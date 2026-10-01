package dsa.graph;

public final class DisjointSet {
    private final int[] parent;
    private final int[] size;
    private int components;

    public DisjointSet(int elements) {
        if (elements < 0) throw new IllegalArgumentException("Element count cannot be negative");
        parent = new int[elements];
        size = new int[elements];
        components = elements;
        for (int i = 0; i < elements; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int components() { return components; }

    public int find(int element) {
        checkElement(element);
        int root = element;
        while (root != parent[root]) root = parent[root];
        while (element != root) {
            int next = parent[element];
            parent[element] = root;
            element = next;
        }
        return root;
    }

    public boolean union(int first, int second) {
        int rootFirst = find(first), rootSecond = find(second);
        if (rootFirst == rootSecond) return false;
        if (size[rootFirst] < size[rootSecond]) {
            int temporary = rootFirst;
            rootFirst = rootSecond;
            rootSecond = temporary;
        }
        parent[rootSecond] = rootFirst;
        size[rootFirst] += size[rootSecond];
        components--;
        return true;
    }

    public boolean connected(int first, int second) { return find(first) == find(second); }

    private void checkElement(int element) {
        if (element < 0 || element >= parent.length) {
            throw new IndexOutOfBoundsException("Element: " + element);
        }
    }
}
