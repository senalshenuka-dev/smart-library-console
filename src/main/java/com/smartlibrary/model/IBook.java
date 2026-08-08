package com.smartlibrary.model;

import java.util.List;

// IBook interface used by Book and decorators.

public interface IBook {
    String getBookId();
    String getTitle();
    String getAuthor();
    String getCategory();
    String getIsbn();
    com.smartlibrary.model.state.IBookState getState();
    void setState(com.smartlibrary.model.state.IBookState state);
    List<BorrowRecord> getBorrowHistory();
    void addBorrowRecord(BorrowRecord r);
    // helper to unwrap core book from decorators
    Book getCore();
}