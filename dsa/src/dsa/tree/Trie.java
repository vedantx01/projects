package dsa.tree;

public final class Trie {
    private static final class Node {
        Node[] children = new Node[26];
        boolean terminal;
    }

    private final Node root = new Node();
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void insert(String word) {
        validate(word);
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (node.children[index] == null) node.children[index] = new Node();
            node = node.children[index];
        }
        if (!node.terminal) {
            node.terminal = true;
            size++;
        }
    }

    public boolean contains(String word) {
        Node node = find(word);
        return node != null && node.terminal;
    }

    public boolean startsWith(String prefix) { return find(prefix) != null; }

    public boolean remove(String word) {
        validate(word);
        if (!contains(word)) return false;
        remove(root, word, 0);
        size--;
        return true;
    }

    private boolean remove(Node node, String word, int depth) {
        if (depth == word.length()) node.terminal = false;
        else {
            int index = word.charAt(depth) - 'a';
            if (remove(node.children[index], word, depth + 1)) node.children[index] = null;
        }
        if (node == root || node.terminal) return false;
        for (Node child : node.children) if (child != null) return false;
        return true;
    }

    private Node find(String word) {
        validate(word);
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            node = node.children[word.charAt(i) - 'a'];
            if (node == null) return null;
        }
        return node;
    }

    private void validate(String word) {
        if (word == null) throw new IllegalArgumentException("Word cannot be null");
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);
            if (character < 'a' || character > 'z') {
                throw new IllegalArgumentException("Trie accepts lowercase a-z only");
            }
        }
    }
}
