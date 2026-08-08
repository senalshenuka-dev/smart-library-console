package com.smartlibrary.observer;

import com.smartlibrary.model.*;
import com.smartlibrary.repository.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


// NotificationService implements Subject (Observer pattern).

public class NotificationService implements ISubject {
    private final List<IObserver> observers = new CopyOnWriteArrayList<>();
    private static final NotificationService INST = new NotificationService();
    private final com.smartlibrary.repository.ReservationRepository reservationRepo = com.smartlibrary.repository.ReservationRepository.getInstance();
    private final com.smartlibrary.repository.BookRepository bookRepo = com.smartlibrary.repository.BookRepository.getInstance();
    private final com.smartlibrary.repository.UserRepository userRepo = com.smartlibrary.repository.UserRepository.getInstance();

    private NotificationService() {}

    public static NotificationService getInstance() { return INST; }

    public void attach(IObserver o) { observers.add(o); }

    public void detach(IObserver o) { observers.remove(o); }

    public void notifyAllObservers(Notification n) {
        for (IObserver o : observers) o.update(n);
    }

    // notify specific user by id
    public void notifyUser(String userId, Notification n) {
        userRepo.findById(userId).ifPresent(u -> u.update(n));
    }

    // call when a book is returned: notify earliest reservation
    public void handleBookReturned(String bookId) {
        List<Reservation> reservations = reservationRepo.findByBookId(bookId);
        if (reservations.isEmpty()) return;
        // notify first reservation
        Reservation earliest = reservations.get(0);
        Notification n = new Notification("Reserved book available", "Book " + bookId + " is now available. Reservation: " + earliest.getReservationId());
        notifyUser(earliest.getUserId(), n);
        earliest.markNotified();
        // optional: remove reservation after notify
        reservationRepo.remove(earliest.getReservationId());
    }

    // periodic check for overdue and notify (simplified call manual)
    public void checkAndNotifyOverdues() {
        bookRepo.findAll().forEach(b -> b.getBorrowHistory().forEach(br -> {
            if (br.isOverdue()) {
                Notification n = new Notification("Overdue book", "Book " + br.getBookId() + " is overdue by " + br.daysLate() + " days for user " + br.getUserId());
                notifyUser(br.getUserId(), n);
            }
        }));
    }
}