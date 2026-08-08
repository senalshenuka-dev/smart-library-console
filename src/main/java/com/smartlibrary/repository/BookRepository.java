package com.smartlibrary.repository;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.IBook;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Simple in-memory repository for Books.

public class BookRepository {
    private final Map<String, IBook> store = new ConcurrentHashMap<>();
    private static final BookRepository INST = new BookRepository();

    private BookRepository() {}

    public static BookRepository getInstance() { return INST; }

    public void save(IBook book) {
        store.put(book.getBookId(), book);
    }

    public Optional<IBook> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<IBook> findAll() {
        return new ArrayList<>(store.values());
    }

    public void remove(String id) {
        store.remove(id);
    }

    public List<IBook> findMostBorrowed(int limit) {
        return store.values().stream()
                .sorted(Comparator.comparingInt((IBook b) -> b.getBorrowHistory().size()).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}