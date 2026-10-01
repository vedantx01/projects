package com.lowleveldesign.subjects.database_and_persistence.repository;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void save(Book book);

    Optional<Book> findById(String id);

    List<Book> findAll();
}
