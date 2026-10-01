package dsa.array;

public final class DynamicArrayInt {
    private int[] values;
    private int size;

    public DynamicArrayInt() {
        this(8);
    }

    public DynamicArrayInt(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        values = new int[Math.max(1, initialCapacity)];
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public int get(int index) {
        checkElementIndex(index);
        return values[index];
    }

    public void set(int index, int value) {
        checkElementIndex(index);
        values[index] = value;
    }

    public void add(int value) {
        ensureCapacity(size + 1);
        values[size++] = value;
    }

    public void insert(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) values[i] = values[i - 1];
        values[index] = value;
        size++;
    }

    public int removeAt(int index) {
        checkElementIndex(index);
        int removed = values[index];
        for (int i = index; i < size - 1; i++) values[i] = values[i + 1];
        size--;
        return removed;
    }

    public int indexOf(int value) {
        for (int i = 0; i < size; i++) if (values[i] == value) return i;
        return -1;
    }

    public void reverse() {
        for (int left = 0, right = size - 1; left < right; left++, right--) {
            int value = values[left];
            values[left] = values[right];
            values[right] = value;
        }
    }

    public int[] toArray() {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) result[i] = values[i];
        return result;
    }

    private void ensureCapacity(int required) {
        if (required <= values.length) return;
        int capacity = values.length;
        while (capacity < required) {
            if (capacity > Integer.MAX_VALUE / 2) {
                capacity = required;
                break;
            }
            capacity *= 2;
        }
        int[] expanded = new int[capacity];
        for (int i = 0; i < size; i++) expanded[i] = values[i];
        values = expanded;
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }
}
