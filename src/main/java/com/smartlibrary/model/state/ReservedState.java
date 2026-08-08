package com.smartlibrary.model.state;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.User;


// Reserved state: awaiting pickup by reserver when returned.

public class ReservedState implements IBookState {
    public boolean borrow(Book book, User user) {
        // reserved; may have specific reserver; simplified: allow borrow
        book.setState(new com.smartlibrary.model.state.BorrowedState());
        return true;
    }

    public boolean returnBook(Book book, User user) {
        // if reserved, when returned we usually notify reserver and keep reserved
        return false;
    }

    public boolean reserve(Book book, User user) {
        // already reserved
        return false;
    }

    public String getStatus() {
        return "RESERVED";
    }
}