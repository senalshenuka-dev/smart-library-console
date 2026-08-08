package com.smartlibrary.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a user's reservation for a book.
 *
 * Reservation IDs are now generated in a well-formatted, human-friendly pattern:
 *   RES-YYYYMMDD-HHMMSS-XXXX
 * where XXXX is a random alphanumeric suffix to reduce collision chance.
 *
 * Example: RES-20251209-142530-A7F3
 */
public class Reservation {
    private final String reservationId;
    private final String bookId;
    private final String userId;
    private final LocalDateTime reservedAt;
    private boolean notified = false;

    public Reservation(String bookId, String userId) {
        this.reservationId = generateFormattedId();
        this.bookId = bookId;
        this.userId = userId;
        this.reservedAt = LocalDateTime.now();
    }

    /**
     * Generates a readable reservation id using timestamp + short random suffix.
     */
    private String generateFormattedId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String timestamp = now.format(dtf);
        String suffix = randomAlphanumeric(4);
        return "RES-" + timestamp + "-" + suffix;
    }

    /**
     * Generates a random uppercase alphanumeric string of given length.
     */
    private String randomAlphanumeric(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public String getReservationId() { return reservationId; }
    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public LocalDateTime getReservedAt() { return reservedAt; }
    public boolean isNotified() { return notified; }
    public void markNotified() { notified = true; }

    public String toString() {
        return String.format("Reservation[%s book=%s user=%s at=%s notified=%s]", reservationId, bookId, userId, reservedAt, notified);
    }
}