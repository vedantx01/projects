package dsa.tree;

public final class AvlTreeInt {
    private static final class Node {
        int value;
        int height;
        Node left;
        Node right;
        Node(int value) { this.value = value; height = 1; }
    }

    private Node root;
    private int size;

    public int size() { return size; }
    public boolean contains(int value) {
        Node node = root;
        while (node != null) {
            if (value == node.value) return true;
            node = value < node.value ? node.left : node.right;
        }
        return false;
    }

    public boolean insert(int value) {
        if (contains(value)) return false;
        root = insert(root, value);
        size++;
        return true;
    }

    public boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        size--;
        return true;
    }

    public int height() { return nodeHeight(root); }
    public int[] inOrder() { int[] out = new int[size]; fill(root, out, new int[1]); return out; }

    private Node insert(Node node, int value) {
        if (node == null) return new Node(value);
        if (value < node.value) node.left = insert(node.left, value);
        else node.right = insert(node.right, value);
        return rebalance(node);
    }

    private Node remove(Node node, int value) {
        if (value < node.value) node.left = remove(node.left, value);
        else if (value > node.value) node.right = remove(node.right, value);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return rebalance(node);
    }

    private Node rebalance(Node node) {
        updateHeight(node);
        int balance = balance(node);
        if (balance > 1) {
            if (balance(node.left) < 0) node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1) {
            if (balance(node.right) > 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private Node rotateRight(Node root) {
        Node pivot = root.left;
        root.left = pivot.right;
        pivot.right = root;
        updateHeight(root);
        updateHeight(pivot);
        return pivot;
    }

    private Node rotateLeft(Node root) {
        Node pivot = root.right;
        root.right = pivot.left;
        pivot.left = root;
        updateHeight(root);
        updateHeight(pivot);
        return pivot;
    }

    private int balance(Node node) { return node == null ? 0 : nodeHeight(node.left) - nodeHeight(node.right); }
    private int nodeHeight(Node node) { return node == null ? 0 : node.height; }
    private void updateHeight(Node node) { node.height = 1 + Math.max(nodeHeight(node.left), nodeHeight(node.right)); }

    private void fill(Node node, int[] out, int[] index) {
        if (node == null) return;
        fill(node.left, out, index);
        out[index[0]++] = node.value;
        fill(node.right, out, index);
    }
}
