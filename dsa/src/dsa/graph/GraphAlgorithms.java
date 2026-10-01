package dsa.graph;

public final class GraphAlgorithms {
    public static final int INF = Integer.MAX_VALUE;
    private GraphAlgorithms() { }

    public static int[] breadthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int[] order = new int[graph.vertexCount()];
        int[] queue = new int[graph.vertexCount()];
        boolean[] visited = new boolean[graph.vertexCount()];
        int head = 0, tail = 0, count = 0;
        queue[tail++] = start;
        visited[start] = true;
        while (count < tail) {
            int vertex = queue[head++];
            order[count++] = vertex;
            for (int next = 0; next < graph.vertexCount(); next++) {
                if (graph.hasEdge(vertex, next) && !visited[next]) {
                    visited[next] = true;
                    queue[tail++] = next;
                }
            }
        }
        return copyPrefix(order, count);
    }

    public static int[] depthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int vertices = graph.vertexCount();
        int[] order = new int[vertices], stack = new int[vertices];
        boolean[] visited = new boolean[vertices];
        int top = 0, count = 0;
        stack[top++] = start;
        visited[start] = true;
        while (top > 0) {
            int vertex = stack[--top];
            order[count++] = vertex;
            for (int next = vertices - 1; next >= 0; next--) {
                if (graph.hasEdge(vertex, next) && !visited[next]) {
                    visited[next] = true;
                    stack[top++] = next;
                }
            }
        }
        return copyPrefix(order, count);
    }

    public static boolean hasCycle(Graph graph) {
        if (graph.isDirected()) {
            byte[] state = new byte[graph.vertexCount()];
            for (int vertex = 0; vertex < graph.vertexCount(); vertex++) {
                if (state[vertex] == 0 && directedCycle(graph, vertex, state)) return true;
            }
            return false;
        }
        boolean[] visited = new boolean[graph.vertexCount()];
        for (int vertex = 0; vertex < graph.vertexCount(); vertex++) {
            if (!visited[vertex] && undirectedCycle(graph, vertex, -1, visited)) return true;
        }
        return false;
    }

    public static int[] topologicalSort(Graph graph) {
        if (!graph.isDirected()) throw new IllegalArgumentException("Topological sort requires a directed graph");
        int vertices = graph.vertexCount();
        int[] indegree = new int[vertices], queue = new int[vertices], order = new int[vertices];
        for (int from = 0; from < vertices; from++) {
            for (int to = 0; to < vertices; to++) if (graph.hasEdge(from, to)) indegree[to]++;
        }
        int head = 0, tail = 0, count = 0;
        for (int vertex = 0; vertex < vertices; vertex++) if (indegree[vertex] == 0) queue[tail++] = vertex;
        while (head < tail) {
            int from = queue[head++];
            order[count++] = from;
            for (int to = 0; to < vertices; to++) {
                if (graph.hasEdge(from, to) && --indegree[to] == 0) queue[tail++] = to;
            }
        }
        if (count != vertices) throw new IllegalStateException("Graph contains a directed cycle");
        return order;
    }

    public static int[] dijkstra(Graph graph, int source) {
        checkStart(graph, source);
        int vertices = graph.vertexCount();
        for (int from = 0; from < vertices; from++) {
            for (int to = 0; to < vertices; to++) {
                if (graph.hasEdge(from, to) && graph.weight(from, to) < 0) {
                    throw new IllegalArgumentException("Dijkstra requires non-negative edge weights");
                }
            }
        }
        int[] distance = new int[vertices];
        boolean[] finalized = new boolean[vertices];
        for (int i = 0; i < vertices; i++) distance[i] = INF;
        distance[source] = 0;
        for (int iteration = 0; iteration < vertices; iteration++) {
            int current = -1;
            for (int vertex = 0; vertex < vertices; vertex++) {
                if (!finalized[vertex] && distance[vertex] != INF
                    && (current < 0 || distance[vertex] < distance[current])) current = vertex;
            }
            if (current < 0) break;
            finalized[current] = true;
            for (int next = 0; next < vertices; next++) {
                if (!graph.hasEdge(current, next)) continue;
                int weight = graph.weight(current, next);
                int candidate = addDistance(distance[current], weight);
                if (candidate < distance[next]) distance[next] = candidate;
            }
        }
        return distance;
    }

    public static int[] bellmanFord(Graph graph, int source) {
        checkStart(graph, source);
        int vertices = graph.vertexCount();
        int[] distance = new int[vertices];
        for (int i = 0; i < vertices; i++) distance[i] = INF;
        distance[source] = 0;
        for (int iteration = 1; iteration < vertices; iteration++) {
            boolean changed = false;
            for (int from = 0; from < vertices; from++) {
                if (distance[from] == INF) continue;
                for (int to = 0; to < vertices; to++) {
                    if (!graph.hasEdge(from, to)) continue;
                    int candidate = addDistance(distance[from], graph.weight(from, to));
                    if (candidate < distance[to]) {
                        distance[to] = candidate;
                        changed = true;
                    }
                }
            }
            if (!changed) break;
        }
        for (int from = 0; from < vertices; from++) {
            if (distance[from] == INF) continue;
            for (int to = 0; to < vertices; to++) {
                if (graph.hasEdge(from, to)
                    && addDistance(distance[from], graph.weight(from, to)) < distance[to]) {
                    throw new IllegalStateException("Reachable negative-weight cycle");
                }
            }
        }
        return distance;
    }

    public static int[][] floydWarshall(Graph graph) {
        int vertices = graph.vertexCount();
        int[][] distance = new int[vertices][vertices];
        for (int from = 0; from < vertices; from++) {
            for (int to = 0; to < vertices; to++) {
                distance[from][to] = from == to ? 0 : INF;
                if (graph.hasEdge(from, to) && graph.weight(from, to) < distance[from][to]) {
                    distance[from][to] = graph.weight(from, to);
                }
            }
        }
        for (int middle = 0; middle < vertices; middle++) {
            for (int from = 0; from < vertices; from++) {
                if (distance[from][middle] == INF) continue;
                for (int to = 0; to < vertices; to++) {
                    if (distance[middle][to] == INF) continue;
                    int candidate = addDistance(distance[from][middle], distance[middle][to]);
                    if (candidate < distance[from][to]) distance[from][to] = candidate;
                }
            }
        }
        for (int vertex = 0; vertex < vertices; vertex++) {
            if (distance[vertex][vertex] < 0) throw new IllegalStateException("Graph contains a negative-weight cycle");
        }
        return distance;
    }

    public static int primMstWeight(Graph graph) {
        requireUndirected(graph);
        int vertices = graph.vertexCount();
        if (vertices == 0) return 0;
        boolean[] selected = new boolean[vertices], hasBest = new boolean[vertices];
        int[] best = new int[vertices];
        best[0] = 0;
        hasBest[0] = true;
        long total = 0;
        for (int count = 0; count < vertices; count++) {
            int current = -1;
            for (int vertex = 0; vertex < vertices; vertex++) {
                if (!selected[vertex] && hasBest[vertex]
                    && (current < 0 || best[vertex] < best[current])) current = vertex;
            }
            if (current < 0) return -1;
            selected[current] = true;
            total += best[current];
            for (int next = 0; next < vertices; next++) {
                if (graph.hasEdge(current, next) && !selected[next]
                    && (!hasBest[next] || graph.weight(current, next) < best[next])) {
                    best[next] = graph.weight(current, next);
                    hasBest[next] = true;
                }
            }
        }
        return checkedWeight(total);
    }

    public static int kruskalMstWeight(Graph graph) {
        requireUndirected(graph);
        int vertices = graph.vertexCount();
        if (vertices == 0) return 0;
        int edgeCount = 0;
        for (int from = 0; from < vertices; from++) {
            for (int to = from + 1; to < vertices; to++) if (graph.hasEdge(from, to)) edgeCount++;
        }
        Edge[] edges = new Edge[edgeCount];
        int index = 0;
        for (int from = 0; from < vertices; from++) {
            for (int to = from + 1; to < vertices; to++) {
                if (graph.hasEdge(from, to)) edges[index++] = new Edge(from, to, graph.weight(from, to));
            }
        }
        sortEdges(edges);
        DisjointSet sets = new DisjointSet(vertices);
        long total = 0;
        int selected = 0;
        for (Edge edge : edges) {
            if (sets.union(edge.from, edge.to)) {
                total += edge.weight;
                if (++selected == vertices - 1) break;
            }
        }
        return selected == vertices - 1 ? checkedWeight(total) : -1;
    }

    private static boolean directedCycle(Graph graph, int vertex, byte[] state) {
        state[vertex] = 1;
        for (int next = 0; next < graph.vertexCount(); next++) {
            if (!graph.hasEdge(vertex, next)) continue;
            if (state[next] == 1 || (state[next] == 0 && directedCycle(graph, next, state))) return true;
        }
        state[vertex] = 2;
        return false;
    }

    private static boolean undirectedCycle(Graph graph, int vertex, int parent, boolean[] visited) {
        visited[vertex] = true;
        for (int next = 0; next < graph.vertexCount(); next++) {
            if (!graph.hasEdge(vertex, next)) continue;
            if (!visited[next]) {
                if (undirectedCycle(graph, next, vertex, visited)) return true;
            } else if (next != parent) return true;
        }
        return false;
    }

    private static int addDistance(int first, int second) {
        long result = (long) first + second;
        if (result >= INF || result < Integer.MIN_VALUE) {
            throw new ArithmeticException("Path distance is outside the supported range");
        }
        return (int) result;
    }

    private static int checkedWeight(long total) {
        if (total < Integer.MIN_VALUE || total > Integer.MAX_VALUE) {
            throw new ArithmeticException("Spanning-tree weight is outside the integer range");
        }
        return (int) total;
    }

    private static void checkStart(Graph graph, int start) {
        if (graph.vertexCount() == 0 || start < 0 || start >= graph.vertexCount()) {
            throw new IndexOutOfBoundsException("Start vertex: " + start);
        }
    }

    private static int[] copyPrefix(int[] values, int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) result[i] = values[i];
        return result;
    }

    private static void requireUndirected(Graph graph) {
        if (graph.isDirected()) throw new IllegalArgumentException("Minimum spanning tree requires an undirected graph");
    }

    private static void sortEdges(Edge[] edges) {
        for (int i = 1; i < edges.length; i++) {
            Edge edge = edges[i];
            int j = i - 1;
            while (j >= 0 && edges[j].weight > edge.weight) {
                edges[j + 1] = edges[j--];
            }
            edges[j + 1] = edge;
        }
    }

    private static final class Edge {
        final int from;
        final int to;
        final int weight;
        Edge(int from, int to, int weight) { this.from = from; this.to = to; this.weight = weight; }
    }
}
