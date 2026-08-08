package com.smartlibrary.commands;

import com.smartlibrary.model.*;
import com.smartlibrary.observer.NotificationService;
import com.smartlibrary.repository.*;
import java.time.LocalDate;
import java.util.Optional;

// Borrow command implements execute/undo.

public class BorrowCommand implements ICommand {
    private final String bookId;
    private final String userId;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final ReservationRepository reservationRepo;
    private final NotificationService notificationService;
    private BorrowRecord appliedRecord;

    public BorrowCommand(String bookId, String userId, BookRepository bookRepo, UserRepository userRepo, ReservationRepository reservationRepo, 
            NotificationService notificationService) {
        this.bookId = bookId; this.userId = userId;
        this.bookRepo = bookRepo; this.userRepo = userRepo; this.reservationRepo = reservationRepo; this.notificationService = notificationService;
    }

    public CommandResult execute() {
        Optional<IBook> ob = bookRepo.findById(bookId);
        if (ob.isEmpty()) return new CommandResult(false, "Book not found.");
        Optional<User> ou = userRepo.findById(userId);
        if (ou.isEmpty()) return new CommandResult(false, "User not found.");

        IBook ib = ob.get();
        User user = ou.get();

        // Borrow limit check
        if (user.getBorrowedRecords().size() >= user.getMembershipType().borrowLimit()) {
            return new CommandResult(false, "Borrow limit reached.");
        }

        // If book is available
        if (!"AVAILABLE".equals(ib.getState().getStatus())) {
            return new CommandResult(false, "Book is not available.");
        }

        // perform borrow
        boolean ok = ib.getState().borrow(ib.getCore(), user);
        if (!ok) return new CommandResult(false, "Failed to borrow (state prevented).");

        LocalDate borrowDate = LocalDate.now();
        LocalDate due = borrowDate.plusDays(user.getMembershipType().dueDays());
        BorrowRecord record = new BorrowRecord(bookId, userId, borrowDate, due);
        ib.addBorrowRecord(record);
        user.addBorrowRecord(record);
        appliedRecord = record;

        // Persist changes (in-memory)
        bookRepo.save(ib);
        userRepo.save(user);

        return new CommandResult(true, "Borrowed. Due date: " + due);
    }

    public CommandResult undo() {
        if (appliedRecord == null) return new CommandResult(false, "Nothing to undo.");
        Optional<IBook> ob = bookRepo.findById(bookId);
        Optional<User> ou = userRepo.findById(userId);
        if (ob.isEmpty() || ou.isEmpty()) return new CommandResult(false, "Book or user missing for undo.");

        IBook ib = ob.get();
        User user = ou.get();

        ib.getState().returnBook(ib.getCore(), user);
        ib.getBorrowHistory().remove(appliedRecord);
        user.getBorrowedRecords().remove(appliedRecord);
        bookRepo.save(ib);
        userRepo.save(user);

        return new CommandResult(true, "Undo borrow completed.");
    }
}