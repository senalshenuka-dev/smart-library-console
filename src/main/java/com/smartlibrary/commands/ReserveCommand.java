package com.smartlibrary.commands;

import com.smartlibrary.model.*;
import com.smartlibrary.repository.*;
import java.util.Optional;


// Reserve command: create a reservation for a book that is borrowed.

public class ReserveCommand implements ICommand {
    private final String bookId;
    private final String userId;
    private final ReservationRepository reservationRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private Reservation created;

    public ReserveCommand(String bookId, String userId, ReservationRepository reservationRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.bookId = bookId; this.userId = userId; this.reservationRepo = reservationRepo; this.bookRepo = bookRepo; this.userRepo = userRepo;
    }

    public CommandResult execute() {
        Optional<IBook> ob = bookRepo.findById(bookId);
        Optional<com.smartlibrary.model.User> ou = userRepo.findById(userId);
        if (ob.isEmpty() || ou.isEmpty()) return new CommandResult(false, "Book or user not found.");

        IBook ib = ob.get();
        if ("AVAILABLE".equals(ib.getState().getStatus())) {
            // book is available, advise borrow instead
            return new CommandResult(false, "Book is available; consider borrowing instead of reserving.");
        }

        Reservation r = new Reservation(bookId, userId);
        reservationRepo.save(r);
        created = r;
        return new CommandResult(true, "Reservation created: " + r.getReservationId());
    }

    public CommandResult undo() {
        if (created == null) return new CommandResult(false, "Nothing to undo.");
        reservationRepo.remove(created.getReservationId());
        return new CommandResult(true, "Reservation canceled (undo).");
    }
}