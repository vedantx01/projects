package com.lowleveldesign.subjects.database_and_persistence.repository;

public final class BookRepositoryDemo {
    private BookRepositoryDemo() {
    }

    public static void run() {
        BookRepository books = new InMemoryBookRepository();
        books.save(new Book("java-1", "Effective Java"));
        System.out.println("Repository: " + books.findById("java-1").orElseThrow().title());
    }
}
