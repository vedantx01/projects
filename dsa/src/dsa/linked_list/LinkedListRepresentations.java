package dsa.linked_list;

public final class LinkedListRepresentations {
    private LinkedListRepresentations() { }

    public static final class SparseMatrix {
        private static final class Entry {
            final int row;
            final int column;
            int value;
            Entry next;
            Entry(int row, int column, int value) { this.row = row; this.column = column; this.value = value; }
        }
        private final int rows;
        private final int columns;
        private Entry head;
        public SparseMatrix(int rows, int columns) {
            if (rows < 0 || columns < 0) throw new IllegalArgumentException("Dimensions cannot be negative");
            this.rows = rows;
            this.columns = columns;
        }
        public int get(int row, int column) {
            checkCell(row, column);
            for (Entry entry = head; entry != null; entry = entry.next) {
                if (entry.row == row && entry.column == column) return entry.value;
            }
            return 0;
        }
        public void set(int row, int column, int value) {
            checkCell(row, column);
            Entry previous = null, current = head;
            while (current != null && (current.row < row || (current.row == row && current.column < column))) {
                previous = current;
                current = current.next;
            }
            if (current != null && current.row == row && current.column == column) {
                if (value == 0) {
                    if (previous == null) head = current.next;
                    else previous.next = current.next;
                } else current.value = value;
            } else if (value != 0) {
                Entry entry = new Entry(row, column, value);
                entry.next = current;
                if (previous == null) head = entry;
                else previous.next = entry;
            }
        }
        public int nonZeroCount() {
            int count = 0;
            for (Entry entry = head; entry != null; entry = entry.next) count++;
            return count;
        }
        private void checkCell(int row, int column) {
            if (row < 0 || row >= rows || column < 0 || column >= columns) {
                throw new IndexOutOfBoundsException("Cell: (" + row + ", " + column + ")");
            }
        }
    }

    public static final class Polynomial {
        private static final class Term {
            int coefficient;
            final int exponent;
            Term next;
            Term(int coefficient, int exponent) { this.coefficient = coefficient; this.exponent = exponent; }
        }
        private Term head;
        public void addTerm(int coefficient, int exponent) {
            if (exponent < 0) throw new IllegalArgumentException("Exponent cannot be negative");
            if (coefficient == 0) return;
            Term previous = null, current = head;
            while (current != null && current.exponent > exponent) {
                previous = current;
                current = current.next;
            }
            if (current != null && current.exponent == exponent) {
                current.coefficient += coefficient;
                if (current.coefficient == 0) {
                    if (previous == null) head = current.next;
                    else previous.next = current.next;
                }
                return;
            }
            Term term = new Term(coefficient, exponent);
            term.next = current;
            if (previous == null) head = term;
            else previous.next = term;
        }
        public Polynomial plus(Polynomial other) {
            Polynomial result = new Polynomial();
            for (Term term = head; term != null; term = term.next) result.addTerm(term.coefficient, term.exponent);
            for (Term term = other.head; term != null; term = term.next) result.addTerm(term.coefficient, term.exponent);
            return result;
        }
        public long evaluate(int x) {
            long result = 0;
            for (Term term = head; term != null; term = term.next) {
                long power = 1;
                for (int i = 0; i < term.exponent; i++) power *= x;
                result += term.coefficient * power;
            }
            return result;
        }
        public int termCount() {
            int count = 0;
            for (Term term = head; term != null; term = term.next) count++;
            return count;
        }
    }

    public static final class AdjacencyList {
        private static final class Edge {
            final int vertex;
            Edge next;
            Edge(int vertex, Edge next) { this.vertex = vertex; this.next = next; }
        }
        private final Edge[] heads;
        private final boolean directed;
        public AdjacencyList(int vertices, boolean directed) {
            if (vertices < 0) throw new IllegalArgumentException("Vertex count cannot be negative");
            heads = new Edge[vertices];
            this.directed = directed;
        }
        public int vertexCount() { return heads.length; }
        public void addEdge(int from, int to) {
            checkVertex(from);
            checkVertex(to);
            heads[from] = new Edge(to, heads[from]);
            if (!directed) heads[to] = new Edge(from, heads[to]);
        }
        public int[] neighbors(int vertex) {
            checkVertex(vertex);
            int count = 0;
            for (Edge edge = heads[vertex]; edge != null; edge = edge.next) count++;
            int[] result = new int[count];
            int index = 0;
            for (Edge edge = heads[vertex]; edge != null; edge = edge.next) result[index++] = edge.vertex;
            return result;
        }
        private void checkVertex(int vertex) {
            if (vertex < 0 || vertex >= heads.length) throw new IndexOutOfBoundsException("Vertex: " + vertex);
        }
    }

    public static final class SkipList {
        private static final int MAX_LEVEL = 16;
        private static final class Node {
            final int value;
            final Node[] next;
            Node(int value, int level) { this.value = value; next = new Node[level]; }
        }
        private final Node head = new Node(0, MAX_LEVEL);
        private int level = 1;
        private long randomState = 0x9e3779b97f4a7c15L;
        public boolean contains(int value) {
            Node current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].value < value) current = current.next[i];
            }
            current = current.next[0];
            return current != null && current.value == value;
        }
        public boolean insert(int value) {
            Node[] update = new Node[MAX_LEVEL];
            Node current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].value < value) current = current.next[i];
                update[i] = current;
            }
            current = current.next[0];
            if (current != null && current.value == value) return false;
            int newLevel = randomLevel();
            if (newLevel > level) {
                for (int i = level; i < newLevel; i++) update[i] = head;
                level = newLevel;
            }
            Node node = new Node(value, newLevel);
            for (int i = 0; i < newLevel; i++) {
                node.next[i] = update[i].next[i];
                update[i].next[i] = node;
            }
            return true;
        }
        public boolean remove(int value) {
            Node[] update = new Node[MAX_LEVEL];
            Node current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].value < value) current = current.next[i];
                update[i] = current;
            }
            current = current.next[0];
            if (current == null || current.value != value) return false;
            for (int i = 0; i < level; i++) {
                if (update[i].next[i] != current) break;
                update[i].next[i] = current.next[i];
            }
            while (level > 1 && head.next[level - 1] == null) level--;
            return true;
        }
        private int randomLevel() {
            int result = 1;
            while (result < MAX_LEVEL) {
                randomState ^= randomState << 13;
                randomState ^= randomState >>> 7;
                randomState ^= randomState << 17;
                if ((randomState & 1) != 0) break;
                result++;
            }
            return result;
        }
    }

    public static final class XorLinkedList {
        private int[] values = new int[8];
        private int[] links = new int[8];
        private int size;
        private int head = -1;
        private int tail = -1;
        public int size() { return size; }
        public void addLast(int value) {
            ensureCapacity();
            int index = size++;
            values[index] = value;
            links[index] = (tail < 0 ? 0 : tail + 1);
            if (tail >= 0) links[tail] ^= index + 1;
            else head = index;
            tail = index;
        }
        public int[] toArray() {
            int[] result = new int[size];
            int previousEncoded = 0, current = head;
            for (int i = 0; i < size; i++) {
                result[i] = values[current];
                int nextEncoded = links[current] ^ previousEncoded;
                previousEncoded = current + 1;
                current = nextEncoded == 0 ? -1 : nextEncoded - 1;
            }
            return result;
        }
        public int[] toArrayReverse() {
            int[] result = new int[size];
            int nextEncoded = 0, current = tail;
            for (int i = 0; i < size; i++) {
                result[i] = values[current];
                int previousEncoded = links[current] ^ nextEncoded;
                nextEncoded = current + 1;
                current = previousEncoded == 0 ? -1 : previousEncoded - 1;
            }
            return result;
        }
        private void ensureCapacity() {
            if (size < values.length) return;
            int[] expandedValues = new int[values.length * 2];
            int[] expandedLinks = new int[links.length * 2];
            for (int i = 0; i < size; i++) {
                expandedValues[i] = values[i];
                expandedLinks[i] = links[i];
            }
            values = expandedValues;
            links = expandedLinks;
        }
    }

    public static final class UnrolledList {
        private static final int BLOCK_SIZE = 8;
        private static final class Block {
            final int[] values = new int[BLOCK_SIZE + 1];
            int size;
            Block next;
        }
        private Block head;
        private int size;
        public int size() { return size; }
        public void add(int index, int value) {
            if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
            if (head == null) head = new Block();
            Block block = head, previous = null;
            int local = index;
            while (block.next != null && local > block.size) {
                local -= block.size;
                previous = block;
                block = block.next;
            }
            if (local > block.size) local = block.size;
            if (block.size == BLOCK_SIZE) {
                Block split = new Block();
                int splitAt = BLOCK_SIZE / 2;
                split.size = block.size - splitAt;
                for (int i = 0; i < split.size; i++) split.values[i] = block.values[splitAt + i];
                block.size = splitAt;
                split.next = block.next;
                block.next = split;
                if (local >= block.size) {
                    local -= block.size;
                    block = split;
                }
            }
            for (int i = block.size; i > local; i--) block.values[i] = block.values[i - 1];
            block.values[local] = value;
            block.size++;
            size++;
        }
        public int get(int index) {
            checkIndex(index);
            Block block = head;
            while (index >= block.size) {
                index -= block.size;
                block = block.next;
            }
            return block.values[index];
        }
        public int remove(int index) {
            checkIndex(index);
            Block block = head, previous = null;
            while (index >= block.size) {
                index -= block.size;
                previous = block;
                block = block.next;
            }
            int removed = block.values[index];
            for (int i = index; i < block.size - 1; i++) block.values[i] = block.values[i + 1];
            block.size--;
            size--;
            if (block.size == 0) {
                if (previous == null) head = block.next;
                else previous.next = block.next;
            }
            return removed;
        }
        public int[] toArray() {
            int[] result = new int[size];
            int index = 0;
            for (Block block = head; block != null; block = block.next) {
                for (int i = 0; i < block.size; i++) result[index++] = block.values[i];
            }
            return result;
        }
        private void checkIndex(int index) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    public static final class SelfOrganizingList {
        private LinkedListAlgorithms.Node head;
        public void add(int value) { head = LinkedListAlgorithms.insertEnd(head, value); }
        public boolean access(int value) {
            if (head == null) return false;
            if (head.value == value) return true;
            LinkedListAlgorithms.Node previous = head;
            while (previous.next != null && previous.next.value != value) previous = previous.next;
            if (previous.next == null) return false;
            LinkedListAlgorithms.Node found = previous.next;
            previous.next = found.next;
            found.next = head;
            head = found;
            return true;
        }
        public int[] toArray() { return LinkedListAlgorithms.traverse(head); }
    }

    public static int[] mergeKSortedArraysFlattened(int[][] sortedLists) {
        int total = 0;
        for (int[] list : sortedLists) total += list.length;
        int[] result = new int[total];
        int[] positions = new int[sortedLists.length];
        for (int output = 0; output < total; output++) {
            int selected = -1;
            for (int i = 0; i < sortedLists.length; i++) {
                if (positions[i] < sortedLists[i].length
                    && (selected < 0 || sortedLists[i][positions[i]] < sortedLists[selected][positions[selected]])) {
                    selected = i;
                }
            }
            result[output] = sortedLists[selected][positions[selected]++];
        }
        return result;
    }
}
