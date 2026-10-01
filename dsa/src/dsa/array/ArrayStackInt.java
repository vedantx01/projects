package dsa.array;

public final class ArrayStackInt {
    private int[] values;
    private int size;

    public ArrayStackInt() { this(8); }

    public ArrayStackInt(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
        values = new int[Math.max(1, capacity)];
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void push(int value) {
        if (size == values.length) {
            int[] expanded = new int[values.length * 2];
            for (int i = 0; i < size; i++) expanded[i] = values[i];
            values = expanded;
        }
        values[size++] = value;
    }

    public int pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return values[--size];
    }

    public int peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return values[size - 1];
    }
}
