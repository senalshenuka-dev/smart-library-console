package com.smartlibrary.commands;

import com.smartlibrary.model.*;
import com.smartlibrary.observer.NotificationService;
import com.smartlibrary.repository.*;
import java.time.LocalDate;
import java.util.Optional;

// Return command: marks return, calculates fine, notifies reservations.

public class ReturnCommand implements ICommand {
    private final String bookId;
    private final String userId;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final ReservationRepository reservationRepo;
    private final NotificationService notificationService;
    private BorrowRecord returnedRecord;
    private java.math.BigDecimal fineCharged = java.math.BigDecimal.ZERO;

    public ReturnCommand(String bookId, String userId, BookRepository bookRepo, UserRepository userRepo, ReservationRepository reservationRepo, NotificationService notificationService) {
        this.bookId = bookId; this.userId = userId; this.bookRepo = bookRepo; this.userRepo = userRepo; this.reservationRepo = reservationRepo; this.notificationService = notificationService;
    }

    public CommandResult execute() {
        Optional<IBook> ob = bookRepo.findById(bookId);
        Optional<User> ou = userRepo.findById(userId);
        if (ob.isEmpty() || ou.isEmpty()) return new CommandResult(false, "Book or user not found.");

        IBook ib = ob.get();
        User user = ou.get();

        // find last borrow record for this user-book without return date
        BorrowRecord target = user.getBorrowedRecords().stream()
                .filter(br -> br.getBookId().equals(bookId) && br.getReturnDate() == null)
                .findFirst().orElse(null);
        if (target == null) return new CommandResult(false, "No active borrow record found for this user and book.");

        // set return date
        LocalDate ret = LocalDate.now();
        target.setReturnDate(ret);
        returnedRecord = target;

        // compute fine
        long lateDays = target.daysLate();
        java.math.BigDecimal fine = user.getFineStrategy().calculateFine(lateDays);
        fineCharged = fine;

        // update book state
        ib.getState().returnBook(ib.getCore(), user);
        bookRepo.save(ib);
        userRepo.save(user);

        // notify reservations
        com.smartlibrary.observer.NotificationService.getInstance().handleBookReturned(bookId);

        String msg = "Returned. Fine: LKR " + fine;
        return new CommandResult(true, msg);
    }

    public CommandResult undo() {
        if (returnedRecord == null) return new CommandResult(false, "Nothing to undo.");
        Optional<IBook> ob = bookRepo.findById(bookId);
        Optional<User> ou = userRepo.findById(userId);
        if (ob.isEmpty() || ou.isEmpty()) return new CommandResult(false, "Book or user missing.");

        IBook ib = ob.get();
        User user = ou.get();
        // revert return
        returnedRecord.setReturnDate(null);
        ib.getState().borrow(ib.getCore(), user);
        bookRepo.save(ib);
        userRepo.save(user);

        return new CommandResult(true, "Undo return completed.");
    }
}