package com.lowleveldesign.subjects.database_and_persistence.repository;

public record Book(String id, String title) {
    public Book {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
    }
}
