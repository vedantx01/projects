package dsa.array;

public final class FenwickTree {
    private final int[] tree;

    public FenwickTree(int[] values) {
        tree = new int[values.length + 1];
        for (int i = 0; i < values.length; i++) add(i, values[i]);
    }

    public int size() { return tree.length - 1; }

    public void add(int index, int delta) {
        checkIndex(index);
        for (int i = index + 1; i < tree.length; i += i & -i) tree[i] += delta;
    }

    public int prefixSum(int endExclusive) {
        if (endExclusive < 0 || endExclusive > size()) {
            throw new IndexOutOfBoundsException("End index: " + endExclusive);
        }
        int sum = 0;
        for (int i = endExclusive; i > 0; i -= i & -i) sum += tree[i];
        return sum;
    }

    public int rangeSum(int startInclusive, int endExclusive) {
        if (startInclusive < 0 || startInclusive > endExclusive || endExclusive > size()) {
            throw new IndexOutOfBoundsException("Range: [" + startInclusive + ", " + endExclusive + ")");
        }
        return prefixSum(endExclusive) - prefixSum(startInclusive);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException("Index: " + index);
    }
}
