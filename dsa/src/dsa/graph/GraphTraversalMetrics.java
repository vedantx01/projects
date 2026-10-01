package dsa.graph;

public final class GraphTraversalMetrics {
    public static final int ESTIMATED_REFERENCE_BYTES = 4;
    public static final int ESTIMATED_BOOLEAN_BYTES = 1;
    public static final int ESTIMATED_RECURSIVE_FRAME_BYTES = 32;

    public static final class Metrics {
        public final String algorithm;
        public final int visitedVertices;
        public final long neighborChecks;
        public final int peakPendingVertices;
        public final int peakRecursiveFrames;
        public final long auxiliaryArrayBytesEstimate;
        public final long auxiliaryPeakBytesEstimate;

        private Metrics(String algorithm, int visitedVertices, long neighborChecks,
                        int peakPendingVertices, int peakRecursiveFrames,
                        long auxiliaryArrayBytesEstimate, long auxiliaryPeakBytesEstimate) {
            this.algorithm = algorithm;
            this.visitedVertices = visitedVertices;
            this.neighborChecks = neighborChecks;
            this.peakPendingVertices = peakPendingVertices;
            this.peakRecursiveFrames = peakRecursiveFrames;
            this.auxiliaryArrayBytesEstimate = auxiliaryArrayBytesEstimate;
            this.auxiliaryPeakBytesEstimate = auxiliaryPeakBytesEstimate;
        }

        @Override
        public String toString() {
            return algorithm + "{visited=" + visitedVertices + ", neighborChecks=" + neighborChecks
                + ", peakPending=" + peakPendingVertices + ", recursiveFrames=" + peakRecursiveFrames
                + ", arrayBytes~" + auxiliaryArrayBytesEstimate + ", peakBytes~" + auxiliaryPeakBytesEstimate + "}";
        }
    }

    private GraphTraversalMetrics() { }

    public static Metrics breadthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int vertices = graph.vertexCount();
        boolean[] visited = new boolean[vertices];
        int[] queue = new int[vertices];
        int head = 0, tail = 0, found = 0, peak = 0;
        long checks = 0;
        queue[tail++] = start;
        visited[start] = true;
        while (head < tail) {
            int current = queue[head++];
            found++;
            for (int next = 0; next < vertices; next++) {
                checks++;
                if (graph.hasEdge(current, next) && !visited[next]) {
                    visited[next] = true;
                    queue[tail++] = next;
                }
            }
            peak = Math.max(peak, tail - head);
        }
        long arrayBytes = (long) vertices * (ESTIMATED_REFERENCE_BYTES + ESTIMATED_BOOLEAN_BYTES);
        return new Metrics("BFS", found, checks, peak, 0, arrayBytes, arrayBytes);
    }

    public static Metrics iterativeDepthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int vertices = graph.vertexCount();
        boolean[] visited = new boolean[vertices];
        int[] stack = new int[vertices];
        int top = 0, found = 0, peak = 0;
        long checks = 0;
        stack[top++] = start;
        visited[start] = true;
        while (top > 0) {
            int current = stack[--top];
            found++;
            for (int next = vertices - 1; next >= 0; next--) {
                checks++;
                if (graph.hasEdge(current, next) && !visited[next]) {
                    visited[next] = true;
                    stack[top++] = next;
                }
            }
            peak = Math.max(peak, top);
        }
        long arrayBytes = (long) vertices * (ESTIMATED_REFERENCE_BYTES + ESTIMATED_BOOLEAN_BYTES);
        return new Metrics("Iterative DFS", found, checks, peak, 0, arrayBytes, arrayBytes);
    }

    public static Metrics recursiveBreadthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int vertices = graph.vertexCount();
        boolean[] visited = new boolean[vertices];
        int[] queue = new int[vertices];
        int[] state = new int[4];
        queue[state[1]++] = start;
        visited[start] = true;
        recursiveBfsVisit(graph, queue, visited, state, 1);
        long arrayBytes = (long) vertices * (ESTIMATED_REFERENCE_BYTES + ESTIMATED_BOOLEAN_BYTES)
            + 4L * Integer.BYTES;
        long frameBytes = (long) state[3] * ESTIMATED_RECURSIVE_FRAME_BYTES;
        return new Metrics("Recursive BFS", state[0], state[2], 0, state[3],
            arrayBytes, arrayBytes + frameBytes);
    }

    public static Metrics recursiveDepthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int vertices = graph.vertexCount();
        boolean[] visited = new boolean[vertices];
        int[] state = new int[3];
        recursiveVisit(graph, start, visited, 1, state);
        long arrayBytes = (long) vertices * ESTIMATED_BOOLEAN_BYTES + 3L * Integer.BYTES;
        long frameBytes = (long) state[2] * ESTIMATED_RECURSIVE_FRAME_BYTES;
        return new Metrics("Recursive DFS", state[0], state[1], 0, state[2],
            arrayBytes, arrayBytes + frameBytes);
    }

    public static long estimatedAdjacencyMatrixBytes(Graph graph) {
        long vertices = graph.vertexCount();
        return vertices * vertices * (ESTIMATED_BOOLEAN_BYTES + Integer.BYTES);
    }

    private static void recursiveVisit(Graph graph, int vertex, boolean[] visited, int depth, int[] state) {
        visited[vertex] = true;
        state[0]++;
        state[2] = Math.max(state[2], depth);
        for (int next = 0; next < graph.vertexCount(); next++) {
            state[1]++;
            if (graph.hasEdge(vertex, next) && !visited[next]) {
                recursiveVisit(graph, next, visited, depth + 1, state);
            }
        }
    }

    private static void recursiveBfsVisit(Graph graph, int[] queue, boolean[] visited, int[] state, int depth) {
        if (state[0] == state[1]) return;
        int current = queue[state[0]++];
        state[3] = Math.max(state[3], depth);
        for (int next = 0; next < graph.vertexCount(); next++) {
            state[2]++;
            if (graph.hasEdge(current, next) && !visited[next]) {
                visited[next] = true;
                queue[state[1]++] = next;
            }
        }
        recursiveBfsVisit(graph, queue, visited, state, depth + 1);
    }

    private static void checkStart(Graph graph, int start) {
        if (graph == null || start < 0 || start >= graph.vertexCount()) {
            throw new IndexOutOfBoundsException("Start vertex: " + start);
        }
    }
}
