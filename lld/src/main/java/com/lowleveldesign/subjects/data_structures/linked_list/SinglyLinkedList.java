package com.lowleveldesign.subjects.data_structures.linked_list;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public final class SinglyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        size++;
    }

    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("list is empty");
        }
        T value = head.value;
        head = head.next;
        size--;
        if (head == null) {
            tail = null;
        }
        return value;
    }

    public int size() {
        return size;
    }

    public List<T> toList() {
        List<T> values = new ArrayList<>(size);
        for (Node<T> node = head; node != null; node = node.next) {
            values.add(node.value);
        }
        return List.copyOf(values);
    }

    private static final class Node<T> {
        private final T value;
        private Node<T> next;

        private Node(T value) {
            this.value = value;
        }
    }
}
