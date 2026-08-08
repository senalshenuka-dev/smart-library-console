package com.smartlibrary.repository;

import com.smartlibrary.model.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// In-memory User repository.

public class UserRepository {
    private final Map<String, User> store = new ConcurrentHashMap<>();
    private static final UserRepository INST = new UserRepository();
    private UserRepository() {}
    public static UserRepository getInstance() { return INST; }

    public void save(User u) { store.put(u.getUserId(), u); }
    public Optional<User> findById(String id) { return Optional.ofNullable(store.get(id)); }
    public List<User> findAll() { return new ArrayList<>(store.values()); }

    public List<User> findActiveBorrowers(int limit) {
        return store.values().stream()
                .sorted(Comparator.comparingInt((User u) -> u.getBorrowedRecords().size()).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}