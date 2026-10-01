package dsa.array;

public final class CircularQueueInt {
    private int[] values;
    private int head;
    private int size;

    public CircularQueueInt() { this(8); }

    public CircularQueueInt(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        values = new int[capacity];
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void offer(int value) {
        if (size == values.length) grow();
        values[(head + size) % values.length] = value;
        size++;
    }

    public int poll() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        int value = values[head];
        head = (head + 1) % values.length;
        size--;
        return value;
    }

    public int peek() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        return values[head];
    }

    private void grow() {
        int[] expanded = new int[values.length * 2];
        for (int i = 0; i < size; i++) expanded[i] = values[(head + i) % values.length];
        values = expanded;
        head = 0;
    }
}
