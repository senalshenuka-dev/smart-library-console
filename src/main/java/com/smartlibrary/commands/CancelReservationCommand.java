package com.smartlibrary.commands;

import com.smartlibrary.model.Reservation;
import com.smartlibrary.repository.ReservationRepository;
import java.util.Optional;


// Cancel reservation by id.

public class CancelReservationCommand implements ICommand {
    private final String reservationId;
    private final ReservationRepository repo;
    private Reservation removed;

    public CancelReservationCommand(String reservationId, ReservationRepository repo) {
        this.reservationId = reservationId; this.repo = repo;
    }

    public CommandResult execute() {
        Optional<Reservation> r = repo.findById(reservationId);
        if (r.isEmpty()) return new CommandResult(false, "Reservation not found.");
        removed = r.get();
        repo.remove(reservationId);
        return new CommandResult(true, "Reservation canceled.");
    }

    public CommandResult undo() {
        if (removed == null) return new CommandResult(false, "Nothing to undo.");
        repo.save(removed);
        return new CommandResult(true, "Undo cancel reservation - restored.");
    }
}