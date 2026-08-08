package com.smartlibrary.repository;

import com.smartlibrary.model.Reservation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//In-memory Reservation repository.

public class ReservationRepository {
    private final Map<String, Reservation> store = new ConcurrentHashMap<>();
    private static final ReservationRepository INST = new ReservationRepository();
    private ReservationRepository() {}
    public static ReservationRepository getInstance() { return INST; }

    public void save(Reservation r) { store.put(r.getReservationId(), r); }
    public Optional<Reservation> findById(String id) { return Optional.ofNullable(store.get(id)); }
    public List<Reservation> findByBookId(String bookId) {
        return store.values().stream().filter(r -> r.getBookId().equals(bookId)).collect(Collectors.toList());
    }
    public void remove(String id) { store.remove(id); }
    public List<Reservation> findAll() { return new ArrayList<>(store.values()); }
}