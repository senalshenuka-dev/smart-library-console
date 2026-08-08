package com.smartlibrary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract decorator for IBook. Allows adding presentation badges/metadata.
 */
public abstract class BookDecorator implements IBook {
    protected final IBook wrapped;

    public BookDecorator(IBook wrapped) {
        this.wrapped = wrapped;
    }

    public String getBookId() { return wrapped.getBookId(); }
    public String getTitle() { return wrapped.getTitle(); }
    public String getAuthor() { return wrapped.getAuthor(); }
    public String getCategory() { return wrapped.getCategory(); }
    public String getIsbn() { return wrapped.getIsbn(); }
    public com.smartlibrary.model.state.IBookState getState() { return wrapped.getState(); }
    public void setState(com.smartlibrary.model.state.IBookState state) { wrapped.setState(state); }
    public List<BorrowRecord> getBorrowHistory() { return wrapped.getBorrowHistory(); }
    public void addBorrowRecord(BorrowRecord r) { wrapped.addBorrowRecord(r); }
    public Book getCore() { return wrapped.getCore(); }

    public List<String> getBadges() {
        return new ArrayList<>();
    }
}