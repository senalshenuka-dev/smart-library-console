package com.smartlibrary.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Borrow record for a borrowed book.
 */
public class BorrowRecord {
    private final String bookId;
    private final String userId;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public BorrowRecord(String bookId, String userId, LocalDate borrowDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate d) { this.returnDate = d; }

    public boolean isOverdue() {
        LocalDate compare = returnDate == null ? LocalDate.now() : returnDate;
        return compare.isAfter(dueDate);
    }

    public long daysLate() {
        LocalDate compare = returnDate == null ? LocalDate.now() : returnDate;
        if (!compare.isAfter(dueDate)) return 0;
        return ChronoUnit.DAYS.between(dueDate, compare);
    }

    public String toString() {
        return String.format("BorrowRecord[%s by %s borrowed=%s due=%s returned=%s]", bookId, userId, borrowDate, dueDate, returnDate);
    }
}