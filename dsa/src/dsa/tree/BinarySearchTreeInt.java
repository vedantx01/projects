package dsa.tree;

public final class BinarySearchTreeInt {
    private static final class Node {
        int value;
        Node left;
        Node right;
        Node(int value) { this.value = value; }
    }

    private Node root;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public boolean insert(int value) {
        if (contains(value)) return false;
        root = insert(root, value);
        size++;
        return true;
    }

    public boolean contains(int value) {
        Node node = root;
        while (node != null) {
            if (value == node.value) return true;
            node = value < node.value ? node.left : node.right;
        }
        return false;
    }

    public boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        size--;
        return true;
    }

    public int min() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        Node node = root;
        while (node.left != null) node = node.left;
        return node.value;
    }

    public int max() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        Node node = root;
        while (node.right != null) node = node.right;
        return node.value;
    }

    public int height() { return height(root); }
    public int[] inOrder() { int[] out = new int[size]; fillInOrder(root, out, new int[1]); return out; }
    public int[] preOrder() { int[] out = new int[size]; fillPreOrder(root, out, new int[1]); return out; }
    public int[] postOrder() { int[] out = new int[size]; fillPostOrder(root, out, new int[1]); return out; }

    private Node insert(Node node, int value) {
        if (node == null) return new Node(value);
        if (value < node.value) node.left = insert(node.left, value);
        else node.right = insert(node.right, value);
        return node;
    }

    private Node remove(Node node, int value) {
        if (value < node.value) node.left = remove(node.left, value);
        else if (value > node.value) node.right = remove(node.right, value);
        else if (node.left == null) return node.right;
        else if (node.right == null) return node.left;
        else {
            Node successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private int height(Node node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    private void fillInOrder(Node node, int[] out, int[] index) {
        if (node == null) return;
        fillInOrder(node.left, out, index);
        out[index[0]++] = node.value;
        fillInOrder(node.right, out, index);
    }

    private void fillPreOrder(Node node, int[] out, int[] index) {
        if (node == null) return;
        out[index[0]++] = node.value;
        fillPreOrder(node.left, out, index);
        fillPreOrder(node.right, out, index);
    }

    private void fillPostOrder(Node node, int[] out, int[] index) {
        if (node == null) return;
        fillPostOrder(node.left, out, index);
        fillPostOrder(node.right, out, index);
        out[index[0]++] = node.value;
    }
}
