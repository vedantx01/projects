package dsa.graph;

public final class Graph {
    private final boolean directed;
    private final boolean[][] adjacent;
    private final int[][] weights;

    public Graph(int vertexCount, boolean directed) {
        if (vertexCount < 0) throw new IllegalArgumentException("Vertex count cannot be negative");
        this.directed = directed;
        adjacent = new boolean[vertexCount][vertexCount];
        weights = new int[vertexCount][vertexCount];
    }

    public int vertexCount() { return adjacent.length; }
    public boolean isDirected() { return directed; }

    public void addEdge(int from, int to) { addEdge(from, to, 1); }

    public void addEdge(int from, int to, int weight) {
        checkVertex(from);
        checkVertex(to);
        adjacent[from][to] = true;
        weights[from][to] = weight;
        if (!directed) {
            adjacent[to][from] = true;
            weights[to][from] = weight;
        }
    }

    public boolean removeEdge(int from, int to) {
        checkVertex(from);
        checkVertex(to);
        boolean existed = adjacent[from][to];
        adjacent[from][to] = false;
        if (!directed) adjacent[to][from] = false;
        return existed;
    }

    public boolean hasEdge(int from, int to) {
        checkVertex(from);
        checkVertex(to);
        return adjacent[from][to];
    }

    public int weight(int from, int to) {
        checkVertex(from);
        checkVertex(to);
        if (!adjacent[from][to]) throw new IllegalArgumentException("No edge from " + from + " to " + to);
        return weights[from][to];
    }

    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= vertexCount()) {
            throw new IndexOutOfBoundsException("Vertex: " + vertex);
        }
    }
}
