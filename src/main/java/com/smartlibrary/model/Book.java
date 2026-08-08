package com.smartlibrary.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Concrete Book implementing IBook and using State pattern for availability.
 */
public class Book implements IBook {
    private final String bookId;
    private final String title;
    private final String author;
    private final String category;
    private final String isbn;
    private com.smartlibrary.model.state.IBookState state;
    private final List<BorrowRecord> borrowHistory = new ArrayList<>();

    public Book(String bookId, String title, String author, String category, String isbn) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.state = new com.smartlibrary.model.state.AvailableState();
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getIsbn() { return isbn; }

    public com.smartlibrary.model.state.IBookState getState() { return state; }
    public void setState(com.smartlibrary.model.state.IBookState state) { this.state = state; }

    public List<BorrowRecord> getBorrowHistory() { return borrowHistory; }
    public void addBorrowRecord(BorrowRecord r) { borrowHistory.add(r); }

    public Book getCore() { return this; }

    public String toString() {
        return String.format("[%s] %s by %s (%s) - %s", bookId, title, author, isbn, state.getStatus());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId);
    }

    public int hashCode() {
        return Objects.hash(bookId);
    }
}