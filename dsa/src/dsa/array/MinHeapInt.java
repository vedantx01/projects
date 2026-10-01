package dsa.array;

public final class MinHeapInt {
    private int[] values;
    private int size;

    public MinHeapInt() { this(8); }

    public MinHeapInt(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
        values = new int[Math.max(1, capacity)];
    }

    public static MinHeapInt heapify(int[] input) {
        MinHeapInt heap = new MinHeapInt(input.length);
        heap.values = new int[Math.max(1, input.length)];
        for (int value : input) heap.values[heap.size++] = value;
        for (int i = heap.size / 2 - 1; i >= 0; i--) heap.siftDown(i);
        return heap;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public int peek() {
        if (isEmpty()) throw new IllegalStateException("Heap is empty");
        return values[0];
    }

    public void insert(int value) {
        ensureCapacity();
        int index = size++;
        values[index] = value;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (values[parent] <= values[index]) break;
            swap(parent, index);
            index = parent;
        }
    }

    public int extractMin() {
        if (isEmpty()) throw new IllegalStateException("Heap is empty");
        int minimum = values[0];
        values[0] = values[--size];
        if (size > 0) siftDown(0);
        return minimum;
    }

    public static void heapSort(int[] values) {
        MinHeapInt heap = heapify(values);
        for (int i = 0; i < values.length; i++) values[i] = heap.extractMin();
    }

    private void siftDown(int index) {
        while (true) {
            int left = index * 2 + 1, right = left + 1, smallest = index;
            if (left < size && values[left] < values[smallest]) smallest = left;
            if (right < size && values[right] < values[smallest]) smallest = right;
            if (smallest == index) return;
            swap(index, smallest);
            index = smallest;
        }
    }

    private void ensureCapacity() {
        if (size < values.length) return;
        int[] expanded = new int[values.length * 2];
        for (int i = 0; i < size; i++) expanded[i] = values[i];
        values = expanded;
    }

    private void swap(int first, int second) {
        int value = values[first];
        values[first] = values[second];
        values[second] = value;
    }
}
