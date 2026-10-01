package dsa.linked_list;

public final class SinglyLinkedListInt {
    private static final class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    private Node head;
    private Node tail;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void addFirst(int value) {
        Node node = new Node(value);
        node.next = head;
        head = node;
        if (tail == null) tail = node;
        size++;
    }

    public void addLast(int value) {
        Node node = new Node(value);
        if (tail == null) head = node;
        else tail.next = node;
        tail = node;
        size++;
    }

    public void insert(int index, int value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == 0) { addFirst(value); return; }
        if (index == size) { addLast(value); return; }
        Node previous = nodeAt(index - 1);
        Node node = new Node(value);
        node.next = previous.next;
        previous.next = node;
        size++;
    }

    public int get(int index) { return nodeAt(index).value; }

    public int removeFirst() {
        if (head == null) throw new IllegalStateException("List is empty");
        int value = head.value;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return value;
    }

    public int removeLast() {
        if (tail == null) throw new IllegalStateException("List is empty");
        if (head == tail) return removeFirst();
        Node previous = nodeAt(size - 2);
        int value = tail.value;
        previous.next = null;
        tail = previous;
        size--;
        return value;
    }

    public boolean removeValue(int value) {
        if (head == null) return false;
        if (head.value == value) { removeFirst(); return true; }
        Node previous = head;
        while (previous.next != null && previous.next.value != value) previous = previous.next;
        if (previous.next == null) return false;
        if (previous.next == tail) tail = previous;
        previous.next = previous.next.next;
        size--;
        return true;
    }

    public int indexOf(int value) {
        int index = 0;
        for (Node node = head; node != null; node = node.next, index++) {
            if (node.value == value) return index;
        }
        return -1;
    }

    public void reverse() {
        Node previous = null;
        Node current = head;
        tail = head;
        while (current != null) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        head = previous;
    }

    public int middle() {
        if (head == null) throw new IllegalStateException("List is empty");
        Node slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow.value;
    }

    public boolean hasCycle() {
        Node slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    public int[] toArray() {
        int[] result = new int[size];
        int index = 0;
        for (Node node = head; node != null; node = node.next) result[index++] = node.value;
        return result;
    }

    private Node nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        Node node = head;
        for (int i = 0; i < index; i++) node = node.next;
        return node;
    }
}
