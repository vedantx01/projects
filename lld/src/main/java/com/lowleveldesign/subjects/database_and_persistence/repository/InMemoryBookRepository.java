package com.lowleveldesign.subjects.database_and_persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryBookRepository implements BookRepository {
    private final ConcurrentMap<String, Book> books = new ConcurrentHashMap<>();

    @Override
    public void save(Book book) {
        books.put(book.id(), book);
    }

    @Override
    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    @Override
    public List<Book> findAll() {
        return books.values().stream()
                .sorted((first, second) -> first.id().compareTo(second.id()))
                .toList();
    }
}
