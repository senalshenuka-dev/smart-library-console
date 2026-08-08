package com.smartlibrary.model.state;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.User;

// Borrowed state: can be returned or result in reservation by others.

public class BorrowedState implements IBookState {
    public boolean borrow(Book book, User user) {
        // cannot borrow while borrowed
        return false;
    }

    public boolean returnBook(Book book, User user) {
        book.setState(new com.smartlibrary.model.state.AvailableState());
        return true;
    }

    public boolean reserve(Book book, User user) {
        book.setState(new com.smartlibrary.model.state.ReservedState());
        return true;
    }

    public String getStatus() {
        return "BORROWED";
    }
}