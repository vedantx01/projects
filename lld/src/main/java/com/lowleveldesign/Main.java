package com.lowleveldesign;

import com.lowleveldesign.subjects.concurrency_design.producer_consumer.BoundedBufferDemo;
import com.lowleveldesign.subjects.data_structures.cache.LruCacheDemo;
import com.lowleveldesign.subjects.data_structures.linked_list.SinglyLinkedListDemo;
import com.lowleveldesign.subjects.database_and_persistence.repository.BookRepositoryDemo;
import com.lowleveldesign.subjects.design_patterns.creational.factory.NotificationFactoryDemo;
import com.lowleveldesign.subjects.solid_and_design_principles.open_closed.OpenClosedDemo;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Low-Level Design in Java ===");
        OpenClosedDemo.run();
        NotificationFactoryDemo.run();
        SinglyLinkedListDemo.run();
        LruCacheDemo.run();
        BoundedBufferDemo.run();
        BookRepositoryDemo.run();
        System.out.println();
        System.out.println("Explore src/main/java/com/lowleveldesign/subjects by subject and concept.");
    }
}
