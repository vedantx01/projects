package com.lowleveldesign.subjects.data_structures.linked_list;

public final class SinglyLinkedListDemo {
    private SinglyLinkedListDemo() {
    }

    public static void run() {
        SinglyLinkedList<String> names = new SinglyLinkedList<>();
        names.addLast("Ada");
        names.addLast("Grace");
        System.out.println("Linked list: " + names.toList() + " (size " + names.size() + ")");
    }
}
