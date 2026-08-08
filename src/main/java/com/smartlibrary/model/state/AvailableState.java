package com.smartlibrary.model.state;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.User;

// Available state: book can be borrowed or reserved.

public class AvailableState implements IBookState {
    public boolean borrow(Book book, User user) {
        // simple check: user borrowing limit checked elsewhere
        book.setState(new com.smartlibrary.model.state.BorrowedState());
        return true;
    }

    public boolean returnBook(Book book, User user) {
        // already available
        return false;
    }

    public boolean reserve(Book book, User user) {
        // if available, we can mark reserved (some systems allow immediate borrow instead)
        book.setState(new com.smartlibrary.model.state.ReservedState());
        return true;
    }

    public String getStatus() {
        return "AVAILABLE";
    }
}