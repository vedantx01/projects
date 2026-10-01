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

    public static int[] recursiveDepthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int[] order = new int[graph.vertexCount()];
        boolean[] visited = new boolean[graph.vertexCount()];
        int[] count = {0};
        recursiveDfs(graph, start, visited, order, count);
        return copyPrefix(order, count[0]);
    }

    public static int[] recursiveBreadthFirstSearch(Graph graph, int start) {
        checkStart(graph, start);
        int[] queue = new int[graph.vertexCount()], order = new int[graph.vertexCount()];
        boolean[] visited = new boolean[graph.vertexCount()];
        int[] state = {0, 0};
        queue[state[1]++] = start;
        visited[start] = true;
        recursiveBfs(graph, queue, state, visited, order);
        return copyPrefix(order, state[0]);
    }

    public static int[] multiSourceBreadthFirstSearch(Graph graph, int[] sources) {
        if (graph == null || sources == null) throw new IllegalArgumentException("Graph and sources cannot be null");
        int vertices = graph.vertexCount(), head = 0, tail = 0, count = 0;
        int[] queue = new int[vertices], order = new int[vertices];
        boolean[] visited = new boolean[vertices];
        for (int source : sources) {
            checkStart(graph, source);
            if (!visited[source]) {
                visited[source] = true;
                queue[tail++] = source;
            }
        }
        while (head < tail) {
            int current = queue[head++];
            order[count++] = current;
            for (int next = 0; next < vertices; next++) {
                if (graph.hasEdge(current, next) && !visited[next]) {
                    visited[next] = true;
                    queue[tail++] = next;
                }
            }
        }
        return copyPrefix(order, count);
    }

    public static int connectedComponentsBfs(Graph graph) {
        requireUndirected(graph);
        return componentCount(graph, false);
    }

    public static int connectedComponentsDfs(Graph graph) {
        requireUndirected(graph);
        return componentCount(graph, true);
    }

    public static boolean hasUndirectedCycleBfs(Graph graph) {
        requireUndirected(graph);
        int vertices = graph.vertexCount();
        boolean[] visited = new boolean[vertices];
        int[] queue = new int[vertices], parent = new int[vertices];
        for (int start = 0; start < vertices; start++) {
            if (visited[start]) continue;
            int head = 0, tail = 0;
            visited[start] = true;
            parent[start] = -1;
            queue[tail++] = start;
            while (head < tail) {
                int current = queue[head++];
                if (graph.hasEdge(current, current)) return true;
                for (int next = 0; next < vertices; next++) {
                    if (!graph.hasEdge(current, next)) continue;
                    if (!visited[next]) {
                        visited[next] = true;
                        parent[next] = current;
                        queue[tail++] = next;
                    } else if (parent[current] != next) return true;
                }
            }
        }
        return false;
    }

    public static boolean hasUndirectedCycleDsu(Graph graph) {
        requireUndirected(graph);
        DisjointSet sets = new DisjointSet(graph.vertexCount());
        for (int from = 0; from < graph.vertexCount(); from++) {
            if (graph.hasEdge(from, from)) return true;
            for (int to = from + 1; to < graph.vertexCount(); to++) {
                if (graph.hasEdge(from, to) && !sets.union(from, to)) return true;
            }
        }
        return false;
    }

    public static int[] topologicalSortDfs(Graph graph) {
        if (!graph.isDirected()) throw new IllegalArgumentException("Topological sort requires a directed graph");
        int vertices = graph.vertexCount();
        byte[] state = new byte[vertices];
        int[] output = new int[vertices], count = {0};
        for (int vertex = 0; vertex < vertices; vertex++) {
            if (state[vertex] == 0 && !topologicalDfs(graph, vertex, state, output, count)) {
                throw new IllegalStateException("Graph contains a directed cycle");
            }
        }
        reverse(output);
        return output;
    }

    public static int[] shortestPathInDag(Graph graph, int source) {
        checkStart(graph, source);
        int[] order = topologicalSortDfs(graph);
        int[] distance = new int[graph.vertexCount()];
        for (int i = 0; i < distance.length; i++) distance[i] = INF;
        distance[source] = 0;
        for (int from : order) {
            if (distance[from] == INF) continue;
            for (int to = 0; to < graph.vertexCount(); to++) {
                if (graph.hasEdge(from, to)) {
                    int candidate = addDistance(distance[from], graph.weight(from, to));
                    if (candidate < distance[to]) distance[to] = candidate;
                }
            }
        }
        return distance;
    }

    public static boolean isBipartite(Graph graph) {
        int vertices = graph.vertexCount();
        int[] colors = new int[vertices], queue = new int[vertices];
        for (int start = 0; start < vertices; start++) {
            if (colors[start] != 0) continue;
            int head = 0, tail = 0;
            colors[start] = 1;
            queue[tail++] = start;
            while (head < tail) {
                int current = queue[head++];
                for (int next = 0; next < vertices; next++) {
                    if (!graph.hasEdge(current, next)) continue;
                    if (colors[next] == 0) {
                        colors[next] = -colors[current];
                        queue[tail++] = next;
                    } else if (colors[next] == colors[current]) return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidTree(Graph graph) {
        requireUndirected(graph);
        int vertices = graph.vertexCount();
        if (vertices == 0) return true;
        int edgeCount = 0;
        for (int from = 0; from < vertices; from++) {
            if (graph.hasEdge(from, from)) return false;
            for (int to = from + 1; to < vertices; to++) if (graph.hasEdge(from, to)) edgeCount++;
        }
        return edgeCount == vertices - 1 && connectedComponentsBfs(graph) == 1;
    }

    public static int[][] stronglyConnectedComponents(Graph graph) {
        if (!graph.isDirected()) throw new IllegalArgumentException("Strongly connected components require a directed graph");
        int vertices = graph.vertexCount();
        boolean[] visited = new boolean[vertices];
        int[] finishOrder = new int[vertices], cursor = {0};
        for (int vertex = 0; vertex < vertices; vertex++) {
            if (!visited[vertex]) finishDfs(graph, vertex, visited, finishOrder, cursor);
        }
        Graph transpose = new Graph(vertices, true);
        for (int from = 0; from < vertices; from++) {
            for (int to = 0; to < vertices; to++) {
                if (graph.hasEdge(from, to)) transpose.addEdge(to, from, graph.weight(from, to));
            }
        }
        for (int i = 0; i < vertices; i++) visited[i] = false;
        int[][] components = new int[vertices][];
        int componentCount = 0;
        for (int i = vertices - 1; i >= 0; i--) {
            int start = finishOrder[i];
            if (visited[start]) continue;
            int[] component = new int[vertices], count = {0};
            recursiveDfs(transpose, start, visited, component, count);
            components[componentCount++] = copyPrefix(component, count[0]);
        }
        int[][] result = new int[componentCount][];
        for (int i = 0; i < componentCount; i++) result[i] = components[i];
        return result;
    }

    public static int[] redundantConnection(int[][] edges, int vertexCount) {
        if (edges == null || vertexCount < 0) throw new IllegalArgumentException("Invalid graph input");
        DisjointSet sets = new DisjointSet(vertexCount);
        for (int[] edge : edges) {
            if (edge == null || edge.length != 2) throw new IllegalArgumentException("Each edge must contain two vertices");
            if (!sets.union(edge[0], edge[1])) return new int[] {edge[0], edge[1]};
        }
        return new int[0];
    }

    public static int numberOfProvinces(boolean[][] connected) {
        if (connected == null) throw new IllegalArgumentException("Matrix cannot be null");
        int vertices = connected.length;
        for (boolean[] row : connected) {
            if (row == null || row.length != vertices) throw new IllegalArgumentException("Matrix must be square");
        }
        DisjointSet sets = new DisjointSet(vertices);
        for (int first = 0; first < vertices; first++) {
            for (int second = first + 1; second < vertices; second++) {
                if (connected[first][second] || connected[second][first]) sets.union(first, second);
            }
        }
        return sets.components();
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

    private static void recursiveDfs(Graph graph, int vertex, boolean[] visited, int[] order, int[] count) {
        visited[vertex] = true;
        order[count[0]++] = vertex;
        for (int next = 0; next < graph.vertexCount(); next++) {
            if (graph.hasEdge(vertex, next) && !visited[next]) recursiveDfs(graph, next, visited, order, count);
        }
    }

    private static void recursiveBfs(Graph graph, int[] queue, int[] state, boolean[] visited, int[] order) {
        if (state[0] == state[1]) return;
        int current = queue[state[0]];
        order[state[0]++] = current;
        for (int next = 0; next < graph.vertexCount(); next++) {
            if (graph.hasEdge(current, next) && !visited[next]) {
                visited[next] = true;
                queue[state[1]++] = next;
            }
        }
        recursiveBfs(graph, queue, state, visited, order);
    }

    private static int componentCount(Graph graph, boolean depthFirst) {
        if (graph == null) throw new IllegalArgumentException("Graph cannot be null");
        int vertices = graph.vertexCount(), components = 0;
        boolean[] visited = new boolean[vertices];
        int[] work = new int[vertices];
        for (int start = 0; start < vertices; start++) {
            if (visited[start]) continue;
            components++;
            int head = 0, tail = 0;
            work[tail++] = start;
            visited[start] = true;
            while (depthFirst ? tail > 0 : head < tail) {
                int current = depthFirst ? work[--tail] : work[head++];
                for (int next = 0; next < vertices; next++) {
                    if (graph.hasEdge(current, next) && !visited[next]) {
                        visited[next] = true;
                        work[tail++] = next;
                    }
                }
            }
        }
        return components;
    }

    private static boolean topologicalDfs(Graph graph, int vertex, byte[] state, int[] output, int[] count) {
        state[vertex] = 1;
        for (int next = 0; next < graph.vertexCount(); next++) {
            if (!graph.hasEdge(vertex, next)) continue;
            if (state[next] == 1 || (state[next] == 0 && !topologicalDfs(graph, next, state, output, count))) {
                return false;
            }
        }
        state[vertex] = 2;
        output[count[0]++] = vertex;
        return true;
    }

    private static void finishDfs(Graph graph, int vertex, boolean[] visited, int[] order, int[] cursor) {
        visited[vertex] = true;
        for (int next = 0; next < graph.vertexCount(); next++) {
            if (graph.hasEdge(vertex, next) && !visited[next]) finishDfs(graph, next, visited, order, cursor);
        }
        order[cursor[0]++] = vertex;
    }

    private static void reverse(int[] values) {
        for (int left = 0, right = values.length - 1; left < right; left++, right--) {
            int value = values[left];
            values[left] = values[right];
            values[right] = value;
        }
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
