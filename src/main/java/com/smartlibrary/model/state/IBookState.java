package com.smartlibrary.model.state;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.User;

//State interface for Book availability.

public interface IBookState {
    boolean borrow(Book book, User user);
    boolean returnBook(Book book, User user);
    boolean reserve(Book book, User user);
    String getStatus();
}