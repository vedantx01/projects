package dsa.array;

public final class HashTableInt {
    private int[] keys;
    private int[] values;
    private byte[] states;
    private int size;

    public HashTableInt() { this(16); }

    public HashTableInt(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        if (capacity > (1 << 30)) throw new IllegalArgumentException("Capacity is too large");
        int tableSize = 1;
        while (tableSize < capacity) tableSize <<= 1;
        keys = new int[tableSize];
        values = new int[tableSize];
        states = new byte[tableSize];
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public boolean containsKey(int key) { return findIndex(key) >= 0; }

    public int get(int key) {
        int index = findIndex(key);
        if (index < 0) throw new IllegalArgumentException("Key not found: " + key);
        return values[index];
    }

    public int getOrDefault(int key, int defaultValue) {
        int index = findIndex(key);
        return index < 0 ? defaultValue : values[index];
    }

    public void put(int key, int value) {
        int existing = findIndex(key);
        if (existing >= 0) {
            values[existing] = value;
            return;
        }
        if ((size + 1) * 10 >= keys.length * 7) resize();
        int index = insertionIndex(key);
        keys[index] = key;
        states[index] = 1;
        size++;
        values[index] = value;
    }

    public boolean remove(int key) {
        int index = findIndex(key);
        if (index < 0) return false;
        states[index] = 2;
        size--;
        return true;
    }

    private int findIndex(int key) {
        int index = mix(key) & (keys.length - 1);
        for (int probes = 0; probes < keys.length; probes++) {
            if (states[index] == 0) return -1;
            if (states[index] == 1 && keys[index] == key) return index;
            index = (index + 1) & (keys.length - 1);
        }
        return -1;
    }

    private int insertionIndex(int key) {
        int index = mix(key) & (keys.length - 1);
        int firstDeleted = -1;
        for (int probes = 0; probes < keys.length; probes++) {
            if (states[index] == 1 && keys[index] == key) return index;
            if (states[index] == 2 && firstDeleted < 0) firstDeleted = index;
            if (states[index] == 0) return firstDeleted >= 0 ? firstDeleted : index;
            index = (index + 1) & (keys.length - 1);
        }
        if (firstDeleted >= 0) return firstDeleted;
        throw new IllegalStateException("Hash table has no available slot");
    }

    private void resize() {
        int[] oldKeys = keys, oldValues = values;
        byte[] oldStates = states;
        if (keys.length >= (1 << 30)) throw new IllegalStateException("Hash table is too large");
        keys = new int[keys.length * 2];
        values = new int[keys.length];
        states = new byte[keys.length];
        size = 0;
        for (int i = 0; i < oldKeys.length; i++) {
            if (oldStates[i] == 1) put(oldKeys[i], oldValues[i]);
        }
    }

    private static int mix(int value) {
        value ^= value >>> 16;
        value *= 0x7feb352d;
        value ^= value >>> 15;
        value *= 0x846ca68b;
        return value ^ (value >>> 16);
    }
}
