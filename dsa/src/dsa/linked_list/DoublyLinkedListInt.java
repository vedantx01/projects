package dsa.linked_list;

public final class DoublyLinkedListInt {
    private static final class Node {
        int value;
        Node previous;
        Node next;
        Node(int value) { this.value = value; }
    }

    private Node head;
    private Node tail;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public int get(int index) { return nodeAt(index).value; }

    public void insert(int index, int value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == 0) { addFirst(value); return; }
        if (index == size) { addLast(value); return; }
        Node following = nodeAt(index);
        Node node = new Node(value);
        node.previous = following.previous;
        node.next = following;
        following.previous.next = node;
        following.previous = node;
        size++;
    }

    public int removeAt(int index) {
        Node node = nodeAt(index);
        if (node.previous == null) return removeFirst();
        if (node.next == null) return removeLast();
        node.previous.next = node.next;
        node.next.previous = node.previous;
        size--;
        return node.value;
    }

    public int indexOf(int value) {
        int index = 0;
        for (Node node = head; node != null; node = node.next, index++) {
            if (node.value == value) return index;
        }
        return -1;
    }

    public int[] toArrayReverse() {
        int[] result = new int[size];
        int i = 0;
        for (Node node = tail; node != null; node = node.previous) result[i++] = node.value;
        return result;
    }

    public void addFirst(int value) {
        Node node = new Node(value);
        node.next = head;
        if (head == null) tail = node;
        else head.previous = node;
        head = node;
        size++;
    }

    public void addLast(int value) {
        Node node = new Node(value);
        node.previous = tail;
        if (tail == null) head = node;
        else tail.next = node;
        tail = node;
        size++;
    }

    public int removeFirst() {
        if (head == null) throw new IllegalStateException("List is empty");
        int value = head.value;
        head = head.next;
        if (head == null) tail = null;
        else head.previous = null;
        size--;
        return value;
    }

    public int removeLast() {
        if (tail == null) throw new IllegalStateException("List is empty");
        int value = tail.value;
        tail = tail.previous;
        if (tail == null) head = null;
        else tail.next = null;
        size--;
        return value;
    }

    public boolean removeValue(int value) {
        Node node = head;
        while (node != null && node.value != value) node = node.next;
        if (node == null) return false;
        if (node.previous == null) head = node.next;
        else node.previous.next = node.next;
        if (node.next == null) tail = node.previous;
        else node.next.previous = node.previous;
        size--;
        return true;
    }

    public void reverse() {
        Node current = head;
        while (current != null) {
            Node next = current.next;
            current.next = current.previous;
            current.previous = next;
            current = next;
        }
        Node oldHead = head;
        head = tail;
        tail = oldHead;
    }

    public int[] toArray() {
        int[] result = new int[size];
        int i = 0;
        for (Node node = head; node != null; node = node.next) result[i++] = node.value;
        return result;
    }

    private Node nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index < size / 2) {
            Node node = head;
            for (int i = 0; i < index; i++) node = node.next;
            return node;
        }
        Node node = tail;
        for (int i = size - 1; i > index; i--) node = node.previous;
        return node;
    }
}
