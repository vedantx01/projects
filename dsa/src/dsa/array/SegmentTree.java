package dsa.array;

public final class SegmentTree {
    private final int length;
    private final int[] tree;

    public SegmentTree(int[] values) {
        length = values.length;
        tree = new int[Math.max(1, length * 4)];
        if (length > 0) build(values, 1, 0, length);
    }

    public int size() { return length; }

    public void update(int index, int value) {
        checkIndex(index);
        update(1, 0, length, index, value);
    }

    public int rangeSum(int startInclusive, int endExclusive) {
        if (startInclusive < 0 || startInclusive > endExclusive || endExclusive > length) {
            throw new IndexOutOfBoundsException("Range: [" + startInclusive + ", " + endExclusive + ")");
        }
        return startInclusive == endExclusive ? 0 : rangeSum(1, 0, length, startInclusive, endExclusive);
    }

    private void build(int[] values, int node, int left, int right) {
        if (right - left == 1) {
            tree[node] = values[left];
            return;
        }
        int middle = (left + right) / 2;
        build(values, node * 2, left, middle);
        build(values, node * 2 + 1, middle, right);
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }

    private void update(int node, int left, int right, int index, int value) {
        if (right - left == 1) {
            tree[node] = value;
            return;
        }
        int middle = (left + right) / 2;
        if (index < middle) update(node * 2, left, middle, index, value);
        else update(node * 2 + 1, middle, right, index, value);
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }

    private int rangeSum(int node, int left, int right, int queryLeft, int queryRight) {
        if (queryLeft <= left && right <= queryRight) return tree[node];
        int middle = (left + right) / 2, sum = 0;
        if (queryLeft < middle) sum += rangeSum(node * 2, left, middle, queryLeft, queryRight);
        if (queryRight > middle) sum += rangeSum(node * 2 + 1, middle, right, queryLeft, queryRight);
        return sum;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= length) throw new IndexOutOfBoundsException("Index: " + index);
    }
}
