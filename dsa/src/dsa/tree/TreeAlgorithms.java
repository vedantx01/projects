package dsa.tree;

public final class TreeAlgorithms {
    public static final class Node {
        public int value;
        public Node left;
        public Node right;
        public Node(int value) { this.value = value; }
    }

    private TreeAlgorithms() { }

    public static int size(Node root) {
        if (root == null) return 0;
        return 1 + size(root.left) + size(root.right);
    }

    public static int height(Node root) {
        if (root == null) return -1;
        return 1 + Math.max(height(root.left), height(root.right));
    }

    public static int depth(Node root, int target) { return depth(root, target, 0); }

    public static int countLeaves(Node root) {
        if (root == null) return 0;
        if (root.left == null && root.right == null) return 1;
        return countLeaves(root.left) + countLeaves(root.right);
    }

    public static int countInternalNodes(Node root) {
        if (root == null || (root.left == null && root.right == null)) return 0;
        return 1 + countInternalNodes(root.left) + countInternalNodes(root.right);
    }

    public static int[] inorder(Node root) {
        int[] output = new int[size(root)];
        fillInorder(root, output, new int[1]);
        return output;
    }

    public static int[] preorder(Node root) {
        int[] output = new int[size(root)];
        fillPreorder(root, output, new int[1]);
        return output;
    }

    public static int[] postorder(Node root) {
        int[] output = new int[size(root)];
        fillPostorder(root, output, new int[1]);
        return output;
    }

    public static int[] inorderIterative(Node root) {
        int[] output = new int[size(root)];
        Node[] nodes = new Node[Math.max(1, output.length)];
        int top = 0, index = 0;
        Node current = root;
        while (current != null || top > 0) {
            while (current != null) {
                nodes[top++] = current;
                current = current.left;
            }
            current = nodes[--top];
            output[index++] = current.value;
            current = current.right;
        }
        return output;
    }

    public static int[] preorderIterative(Node root) {
        if (root == null) return new int[0];
        int[] output = new int[size(root)];
        Node[] stack = new Node[output.length];
        int top = 0, index = 0;
        stack[top++] = root;
        while (top > 0) {
            Node node = stack[--top];
            output[index++] = node.value;
            if (node.right != null) stack[top++] = node.right;
            if (node.left != null) stack[top++] = node.left;
        }
        return output;
    }

    public static int[] postorderIterative(Node root) {
        if (root == null) return new int[0];
        Node[] stack = new Node[size(root)];
        int[] output = new int[stack.length];
        int top = 0, index = output.length - 1;
        stack[top++] = root;
        while (top > 0) {
            Node node = stack[--top];
            output[index--] = node.value;
            if (node.left != null) stack[top++] = node.left;
            if (node.right != null) stack[top++] = node.right;
        }
        return output;
    }

    public static int[] levelOrder(Node root) {
        if (root == null) return new int[0];
        Node[] queue = new Node[size(root)];
        int[] output = new int[queue.length];
        int head = 0, tail = 0, index = 0;
        queue[tail++] = root;
        while (head < tail) {
            Node node = queue[head++];
            output[index++] = node.value;
            if (node.left != null) queue[tail++] = node.left;
            if (node.right != null) queue[tail++] = node.right;
        }
        return output;
    }

    public static int[][] zigzagLevelOrder(Node root) {
        if (root == null) return new int[0][];
        int height = height(root) + 1;
        int[][] levels = new int[height][];
        Node[] queue = new Node[size(root)];
        int head = 0, tail = 0, level = 0;
        queue[tail++] = root;
        while (head < tail) {
            int count = tail - head;
            levels[level] = new int[count];
            for (int i = 0; i < count; i++) {
                Node node = queue[head++];
                int position = (level & 1) == 0 ? i : count - 1 - i;
                levels[level][position] = node.value;
                if (node.left != null) queue[tail++] = node.left;
                if (node.right != null) queue[tail++] = node.right;
            }
            level++;
        }
        return levels;
    }

    public static int[] reverseLevelOrder(Node root) {
        int[] result = levelOrder(root);
        reverse(result);
        return result;
    }

    public static int[] leftView(Node root) { return view(root, true); }
    public static int[] rightView(Node root) { return view(root, false); }

    public static int[] boundaryTraversal(Node root) {
        if (root == null) return new int[0];
        int[] result = new int[size(root)];
        int[] index = {0};
        result[index[0]++] = root.value;
        addLeftBoundary(root.left, result, index);
        addLeaves(root.left, result, index);
        addLeaves(root.right, result, index);
        addRightBoundary(root.right, result, index);
        return copyOf(result, index[0]);
    }

    public static int[][] verticalOrder(Node root) {
        if (root == null) return new int[0][];
        int min = minHorizontal(root, 0), max = maxHorizontal(root, 0);
        int width = max - min + 1;
        int[][] values = new int[width][size(root)];
        int[] counts = new int[width];
        Node[] nodes = new Node[size(root)];
        int[] columns = new int[nodes.length];
        int head = 0, tail = 0;
        nodes[tail] = root;
        columns[tail++] = -min;
        while (head < tail) {
            Node node = nodes[head];
            int column = columns[head++];
            values[column][counts[column]++] = node.value;
            if (node.left != null) { nodes[tail] = node.left; columns[tail++] = column - 1; }
            if (node.right != null) { nodes[tail] = node.right; columns[tail++] = column + 1; }
        }
        int[][] result = new int[width][];
        for (int i = 0; i < width; i++) result[i] = copyOf(values[i], counts[i]);
        return result;
    }

    public static int[][] diagonalOrder(Node root) {
        if (root == null) return new int[0][];
        int diagonals = maxDiagonal(root, 0) + 1;
        int[][] values = new int[diagonals][size(root)];
        int[] counts = new int[diagonals];
        fillDiagonal(root, 0, values, counts);
        int[][] result = new int[diagonals][];
        for (int i = 0; i < diagonals; i++) result[i] = copyOf(values[i], counts[i]);
        return result;
    }

    public static Node buildFromInorderPreorder(int[] inorder, int[] preorder) {
        validateTraversals(inorder, preorder);
        int[] cursor = {0};
        Node root = buildPreorder(inorder, preorder, 0, inorder.length - 1, cursor);
        if (cursor[0] != preorder.length) throw new IllegalArgumentException("Inconsistent traversals");
        return root;
    }

    public static Node buildFromInorderPostorder(int[] inorder, int[] postorder) {
        validateTraversals(inorder, postorder);
        int[] cursor = {postorder.length - 1};
        Node root = buildPostorder(inorder, postorder, 0, inorder.length - 1, cursor);
        if (cursor[0] != -1) throw new IllegalArgumentException("Inconsistent traversals");
        return root;
    }

    public static Node bstFromSortedArray(int[] sorted) {
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] < sorted[i - 1]) throw new IllegalArgumentException("Array must be sorted");
        }
        return sortedBst(sorted, 0, sorted.length);
    }

    public static Node bstFromPreorder(int[] preorder) {
        int[] cursor = {0};
        Node root = buildBstPreorder(preorder, cursor, Long.MIN_VALUE, Long.MAX_VALUE);
        if (cursor[0] != preorder.length) throw new IllegalArgumentException("Invalid BST preorder");
        return root;
    }

    public static String serialize(Node root) {
        StringBuilder builder = new StringBuilder();
        serialize(root, builder);
        return builder.toString();
    }

    public static Node deserialize(String serialized) {
        if (serialized == null) throw new IllegalArgumentException("Serialization cannot be null");
        String[] tokens = serialized.split(",", -1);
        int[] cursor = {0};
        Node root = deserialize(tokens, cursor);
        if (cursor[0] != tokens.length) throw new IllegalArgumentException("Unexpected serialization data");
        return root;
    }

    public static void flattenPreorder(Node root) {
        Node current = root;
        while (current != null) {
            if (current.left != null) {
                Node predecessor = current.left;
                while (predecessor.right != null) predecessor = predecessor.right;
                predecessor.right = current.right;
                current.right = current.left;
                current.left = null;
            }
            current = current.right;
        }
    }

    public static Node mirror(Node root) {
        if (root == null) return null;
        Node temporary = root.left;
        root.left = mirror(root.right);
        root.right = mirror(temporary);
        return root;
    }

    public static Node invert(Node root) { return mirror(root); }

    public static int diameter(Node root) { return diameterHeight(root).diameter; }

    public static boolean isBalanced(Node root) { return balancedHeight(root) >= 0; }

    public static long maximumPathSum(Node root) {
        if (root == null) throw new IllegalArgumentException("Tree cannot be empty");
        long[] best = {Long.MIN_VALUE};
        maxGain(root, best);
        return best[0];
    }

    public static boolean identical(Node first, Node second) {
        if (first == null || second == null) return first == second;
        return first.value == second.value && identical(first.left, second.left) && identical(first.right, second.right);
    }

    public static boolean symmetric(Node root) { return root == null || mirrorEquals(root.left, root.right); }

    public static boolean hasRootToLeafSum(Node root, long target) {
        if (root == null) return false;
        if (root.left == null && root.right == null) return target == root.value;
        return hasRootToLeafSum(root.left, target - root.value)
            || hasRootToLeafSum(root.right, target - root.value);
    }

    public static long sumLeftLeaves(Node root) {
        return sumLeftLeaves(root, false);
    }

    public static int countGoodNodes(Node root) {
        return countGoodNodes(root, Integer.MIN_VALUE);
    }

    public static int[][] rootToLeafPaths(Node root) {
        int paths = countLeaves(root);
        int[][] result = new int[paths][];
        fillPaths(root, new int[Math.max(0, height(root) + 1)], 0, result, new int[1]);
        return result;
    }

    public static Node lowestCommonAncestor(Node root, int first, int second) {
        if (root == null || root.value == first || root.value == second) return root;
        Node left = lowestCommonAncestor(root.left, first, second);
        Node right = lowestCommonAncestor(root.right, first, second);
        if (left != null && right != null) return root;
        return left == null ? right : left;
    }

    public static int distanceBetween(Node root, int first, int second) {
        Node lca = lowestCommonAncestorIfBoth(root, first, second);
        if (lca == null) return -1;
        int firstDepth = depth(lca, first, 0), secondDepth = depth(lca, second, 0);
        return firstDepth < 0 || secondDepth < 0 ? -1 : firstDepth + secondDepth;
    }

    public static int[] ancestors(Node root, int target) {
        int[] result = new int[size(root)];
        int[] count = {0};
        if (!collectAncestors(root, target, result, count)) return new int[0];
        reverse(result, 0, count[0] - 1);
        return copyOf(result, count[0]);
    }

    public static int[] nodesAtDistanceK(Node root, int target, int distance) {
        if (distance < 0) throw new IllegalArgumentException("Distance cannot be negative");
        Node[] path = new Node[size(root)];
        int[] count = {0};
        if (!findPath(root, target, path, count)) return new int[0];
        int[] output = new int[size(root)];
        int[] used = {0};
        for (int i = count[0] - 1; i >= 0; i--) {
            int remaining = distance - (count[0] - 1 - i);
            if (remaining < 0) break;
            collectDistance(path[i], remaining, i + 1 < count[0] ? path[i + 1] : null, output, used);
        }
        return copyOf(output, used[0]);
    }

    public static int countPathSums(Node root, long target) {
        long[] prefix = new long[size(root) + 1];
        return countPathSums(root, 0, target, prefix, new int[] {1});
    }

    public static long maximumRootToLeafSum(Node root) {
        if (root == null) throw new IllegalArgumentException("Tree cannot be empty");
        if (root.left == null && root.right == null) return root.value;
        if (root.left == null) return root.value + maximumRootToLeafSum(root.right);
        if (root.right == null) return root.value + maximumRootToLeafSum(root.left);
        return root.value + Math.max(maximumRootToLeafSum(root.left), maximumRootToLeafSum(root.right));
    }

    public static int longestRootToLeafPathLength(Node root) {
        return root == null ? 0 : 1 + Math.max(longestRootToLeafPathLength(root.left), longestRootToLeafPathLength(root.right));
    }

    public static long leafToLeafMaximumSum(Node root) {
        if (root == null) throw new IllegalArgumentException("Tree cannot be empty");
        long[] best = {Long.MIN_VALUE};
        leafGain(root, best);
        return best[0] == Long.MIN_VALUE ? maximumRootToLeafSum(root) : best[0];
    }

    public static int[] morrisInorder(Node root) {
        int[] output = new int[size(root)];
        int index = 0;
        Node current = root;
        while (current != null) {
            if (current.left == null) {
                output[index++] = current.value;
                current = current.right;
            } else {
                Node predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) predecessor = predecessor.right;
                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    output[index++] = current.value;
                    current = current.right;
                }
            }
        }
        return output;
    }

    public static int[] morrisPreorder(Node root) {
        int[] output = new int[size(root)];
        int index = 0;
        Node current = root;
        while (current != null) {
            if (current.left == null) {
                output[index++] = current.value;
                current = current.right;
            } else {
                Node predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) predecessor = predecessor.right;
                if (predecessor.right == null) {
                    output[index++] = current.value;
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    current = current.right;
                }
            }
        }
        return output;
    }

    public static int[] topView(Node root) { return horizontalView(root, true); }
    public static int[] bottomView(Node root) { return horizontalView(root, false); }

    public static long[] verticalSums(Node root) {
        int[][] columns = verticalOrder(root);
        long[] sums = new long[columns.length];
        for (int i = 0; i < columns.length; i++) for (int value : columns[i]) sums[i] += value;
        return sums;
    }

    public static int maximumWidth(Node root) {
        if (root == null) return 0;
        Node[] queue = new Node[size(root)];
        int head = 0, tail = 0, maximum = 0;
        queue[tail++] = root;
        while (head < tail) {
            int count = tail - head;
            maximum = Math.max(maximum, count);
            for (int i = 0; i < count; i++) {
                Node node = queue[head++];
                if (node.left != null) queue[tail++] = node.left;
                if (node.right != null) queue[tail++] = node.right;
            }
        }
        return maximum;
    }

    public static double[] averageOfLevels(Node root) {
        if (root == null) return new double[0];
        double[] averages = new double[height(root) + 1];
        Node[] queue = new Node[size(root)];
        int head = 0, tail = 0, level = 0;
        queue[tail++] = root;
        while (head < tail) {
            int count = tail - head;
            long sum = 0;
            for (int i = 0; i < count; i++) {
                Node node = queue[head++];
                sum += node.value;
                if (node.left != null) queue[tail++] = node.left;
                if (node.right != null) queue[tail++] = node.right;
            }
            averages[level++] = (double) sum / count;
        }
        return copyOf(averages, level);
    }

    public static long deepestLeavesSum(Node root) {
        if (root == null) return 0;
        Node[] queue = new Node[size(root)];
        int head = 0, tail = 0;
        long sum = 0;
        queue[tail++] = root;
        while (head < tail) {
            int count = tail - head;
            sum = 0;
            for (int i = 0; i < count; i++) {
                Node node = queue[head++];
                sum += node.value;
                if (node.left != null) queue[tail++] = node.left;
                if (node.right != null) queue[tail++] = node.right;
            }
        }
        return sum;
    }

    public static boolean isValidBst(Node root) {
        return validBst(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static Node bstSearch(Node root, int target) {
        Node node = root;
        while (node != null && node.value != target) node = target < node.value ? node.left : node.right;
        return node;
    }

    public static Node bstInsert(Node root, int value) {
        if (root == null) return new Node(value);
        Node node = root;
        while (true) {
            if (value == node.value) return root;
            if (value < node.value) {
                if (node.left == null) { node.left = new Node(value); return root; }
                node = node.left;
            } else {
                if (node.right == null) { node.right = new Node(value); return root; }
                node = node.right;
            }
        }
    }

    public static Node bstDelete(Node root, int value) {
        if (root == null) return null;
        if (value < root.value) root.left = bstDelete(root.left, value);
        else if (value > root.value) root.right = bstDelete(root.right, value);
        else if (root.left == null) return root.right;
        else if (root.right == null) return root.left;
        else {
            Node successor = root.right;
            while (successor.left != null) successor = successor.left;
            root.value = successor.value;
            root.right = bstDelete(root.right, successor.value);
        }
        return root;
    }

    public static Node bstLca(Node root, int first, int second) {
        while (root != null) {
            if (first < root.value && second < root.value) root = root.left;
            else if (first > root.value && second > root.value) root = root.right;
            else return root;
        }
        return null;
    }

    public static int kthSmallestBst(Node root, int k) {
        if (k <= 0 || k > size(root)) throw new IllegalArgumentException("k is out of range");
        Node[] stack = new Node[size(root)];
        int top = 0, visited = 0;
        Node current = root;
        while (current != null || top > 0) {
            while (current != null) { stack[top++] = current; current = current.left; }
            current = stack[--top];
            if (++visited == k) return current.value;
            current = current.right;
        }
        throw new AssertionError("BST traversal ended unexpectedly");
    }

    public static int kthLargestBst(Node root, int k) {
        if (k <= 0 || k > size(root)) throw new IllegalArgumentException("k is out of range");
        Node[] stack = new Node[size(root)];
        int top = 0, visited = 0;
        Node current = root;
        while (current != null || top > 0) {
            while (current != null) { stack[top++] = current; current = current.right; }
            current = stack[--top];
            if (++visited == k) return current.value;
            current = current.left;
        }
        throw new AssertionError("BST traversal ended unexpectedly");
    }

    private static int depth(Node node, int target, int currentDepth) {
        if (node == null) return -1;
        if (node.value == target) return currentDepth;
        int left = depth(node.left, target, currentDepth + 1);
        return left >= 0 ? left : depth(node.right, target, currentDepth + 1);
    }

    private static void fillInorder(Node node, int[] output, int[] index) {
        if (node == null) return;
        fillInorder(node.left, output, index);
        output[index[0]++] = node.value;
        fillInorder(node.right, output, index);
    }

    private static void fillPreorder(Node node, int[] output, int[] index) {
        if (node == null) return;
        output[index[0]++] = node.value;
        fillPreorder(node.left, output, index);
        fillPreorder(node.right, output, index);
    }

    private static void fillPostorder(Node node, int[] output, int[] index) {
        if (node == null) return;
        fillPostorder(node.left, output, index);
        fillPostorder(node.right, output, index);
        output[index[0]++] = node.value;
    }

    private static int[] view(Node root, boolean leftFirst) {
        if (root == null) return new int[0];
        int[] output = new int[height(root) + 1];
        Node[] queue = new Node[size(root)];
        int head = 0, tail = 0, level = 0;
        queue[tail++] = root;
        while (head < tail) {
            int count = tail - head;
            output[level] = queue[head + (leftFirst ? 0 : count - 1)].value;
            level++;
            for (int i = 0; i < count; i++) {
                Node node = queue[head++];
                if (node.left != null) queue[tail++] = node.left;
                if (node.right != null) queue[tail++] = node.right;
            }
        }
        return copyOf(output, level);
    }

    private static void addLeftBoundary(Node node, int[] output, int[] index) {
        while (node != null) {
            if (node.left != null || node.right != null) output[index[0]++] = node.value;
            node = node.left != null ? node.left : node.right;
        }
    }

    private static void addRightBoundary(Node node, int[] output, int[] index) {
        int[] boundary = new int[size(node)];
        int count = 0;
        while (node != null) {
            if (node.left != null || node.right != null) boundary[count++] = node.value;
            node = node.right != null ? node.right : node.left;
        }
        while (count > 0) output[index[0]++] = boundary[--count];
    }

    private static void addLeaves(Node node, int[] output, int[] index) {
        if (node == null) return;
        if (node.left == null && node.right == null) output[index[0]++] = node.value;
        else { addLeaves(node.left, output, index); addLeaves(node.right, output, index); }
    }

    private static int minHorizontal(Node node, int horizontal) {
        if (node == null) return Integer.MAX_VALUE;
        if (node.left == null && node.right == null) return horizontal;
        return Math.min(horizontal, Math.min(minHorizontal(node.left, horizontal - 1), minHorizontal(node.right, horizontal + 1)));
    }

    private static int maxHorizontal(Node node, int horizontal) {
        if (node == null) return Integer.MIN_VALUE;
        if (node.left == null && node.right == null) return horizontal;
        return Math.max(horizontal, Math.max(maxHorizontal(node.left, horizontal - 1), maxHorizontal(node.right, horizontal + 1)));
    }

    private static int maxDiagonal(Node node, int diagonal) {
        if (node == null) return diagonal - 1;
        return Math.max(diagonal, Math.max(maxDiagonal(node.left, diagonal + 1), maxDiagonal(node.right, diagonal)));
    }

    private static void fillDiagonal(Node node, int diagonal, int[][] values, int[] counts) {
        if (node == null) return;
        values[diagonal][counts[diagonal]++] = node.value;
        fillDiagonal(node.left, diagonal + 1, values, counts);
        fillDiagonal(node.right, diagonal, values, counts);
    }

    private static Node buildPreorder(int[] inorder, int[] preorder, int left, int right, int[] cursor) {
        if (left > right) return null;
        if (cursor[0] >= preorder.length) throw new IllegalArgumentException("Inconsistent traversals");
        int value = preorder[cursor[0]++], split = indexOf(inorder, left, right, value);
        if (split < 0) throw new IllegalArgumentException("Inconsistent traversals or duplicate values");
        Node node = new Node(value);
        node.left = buildPreorder(inorder, preorder, left, split - 1, cursor);
        node.right = buildPreorder(inorder, preorder, split + 1, right, cursor);
        return node;
    }

    private static Node buildPostorder(int[] inorder, int[] postorder, int left, int right, int[] cursor) {
        if (left > right) return null;
        if (cursor[0] < 0) throw new IllegalArgumentException("Inconsistent traversals");
        int value = postorder[cursor[0]--], split = indexOf(inorder, left, right, value);
        if (split < 0) throw new IllegalArgumentException("Inconsistent traversals or duplicate values");
        Node node = new Node(value);
        node.right = buildPostorder(inorder, postorder, split + 1, right, cursor);
        node.left = buildPostorder(inorder, postorder, left, split - 1, cursor);
        return node;
    }

    private static Node sortedBst(int[] values, int left, int right) {
        if (left >= right) return null;
        int middle = left + (right - left) / 2;
        Node node = new Node(values[middle]);
        node.left = sortedBst(values, left, middle);
        node.right = sortedBst(values, middle + 1, right);
        return node;
    }

    private static Node buildBstPreorder(int[] values, int[] cursor, long minimum, long maximum) {
        if (cursor[0] == values.length) return null;
        int value = values[cursor[0]];
        if (value <= minimum || value >= maximum) return null;
        cursor[0]++;
        Node node = new Node(value);
        node.left = buildBstPreorder(values, cursor, minimum, value);
        node.right = buildBstPreorder(values, cursor, value, maximum);
        return node;
    }

    private static void serialize(Node node, StringBuilder output) {
        if (output.length() > 0) output.append(',');
        if (node == null) { output.append('#'); return; }
        output.append(node.value);
        serialize(node.left, output);
        serialize(node.right, output);
    }

    private static Node deserialize(String[] tokens, int[] cursor) {
        if (cursor[0] >= tokens.length) throw new IllegalArgumentException("Incomplete serialization");
        String token = tokens[cursor[0]++];
        if (token.equals("#")) return null;
        Node node;
        try { node = new Node(Integer.parseInt(token)); }
        catch (NumberFormatException exception) { throw new IllegalArgumentException("Invalid node value: " + token, exception); }
        node.left = deserialize(tokens, cursor);
        node.right = deserialize(tokens, cursor);
        return node;
    }

    private static DiameterHeight diameterHeight(Node node) {
        if (node == null) return new DiameterHeight(-1, 0);
        DiameterHeight left = diameterHeight(node.left), right = diameterHeight(node.right);
        int h = 1 + Math.max(left.height, right.height);
        int d = Math.max(left.height + right.height + 2, Math.max(left.diameter, right.diameter));
        return new DiameterHeight(h, d);
    }

    private static int balancedHeight(Node node) {
        if (node == null) return 0;
        int left = balancedHeight(node.left);
        if (left < 0) return -1;
        int right = balancedHeight(node.right);
        if (right < 0 || Math.abs(left - right) > 1) return -1;
        return 1 + Math.max(left, right);
    }

    private static long maxGain(Node node, long[] best) {
        if (node == null) return 0;
        long left = Math.max(0, maxGain(node.left, best)), right = Math.max(0, maxGain(node.right, best));
        best[0] = Math.max(best[0], left + right + node.value);
        return Math.max(left, right) + node.value;
    }

    private static boolean mirrorEquals(Node first, Node second) {
        if (first == null || second == null) return first == second;
        return first.value == second.value && mirrorEquals(first.left, second.right) && mirrorEquals(first.right, second.left);
    }

    private static long sumLeftLeaves(Node node, boolean leftChild) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return leftChild ? node.value : 0;
        return sumLeftLeaves(node.left, true) + sumLeftLeaves(node.right, false);
    }

    private static int countGoodNodes(Node node, int maximum) {
        if (node == null) return 0;
        int nextMaximum = Math.max(maximum, node.value);
        return (node.value >= maximum ? 1 : 0)
            + countGoodNodes(node.left, nextMaximum) + countGoodNodes(node.right, nextMaximum);
    }

    private static void fillPaths(Node node, int[] path, int depth, int[][] output, int[] pathIndex) {
        if (node == null) return;
        path[depth] = node.value;
        if (node.left == null && node.right == null) output[pathIndex[0]++] = copyOf(path, depth + 1);
        else { fillPaths(node.left, path, depth + 1, output, pathIndex); fillPaths(node.right, path, depth + 1, output, pathIndex); }
    }

    private static Node lowestCommonAncestorIfBoth(Node node, int first, int second) {
        Node lca = lowestCommonAncestor(node, first, second);
        if (lca == null || depth(lca, first, 0) < 0 || depth(lca, second, 0) < 0) return null;
        return lca;
    }

    private static boolean collectAncestors(Node node, int target, int[] output, int[] count) {
        if (node == null) return false;
        if (node.value == target) return true;
        if (collectAncestors(node.left, target, output, count) || collectAncestors(node.right, target, output, count)) {
            output[count[0]++] = node.value;
            return true;
        }
        return false;
    }

    private static boolean findPath(Node node, int target, Node[] path, int[] count) {
        if (node == null) return false;
        path[count[0]++] = node;
        if (node.value == target || findPath(node.left, target, path, count) || findPath(node.right, target, path, count)) return true;
        count[0]--;
        return false;
    }

    private static void collectDistance(Node node, int distance, Node blocked, int[] output, int[] used) {
        if (node == null || node == blocked) return;
        if (distance == 0) { output[used[0]++] = node.value; return; }
        collectDistance(node.left, distance - 1, blocked, output, used);
        collectDistance(node.right, distance - 1, blocked, output, used);
    }

    private static int countPathSums(Node node, long sum, long target, long[] prefix, int[] used) {
        if (node == null) return 0;
        sum += node.value;
        int count = 0;
        for (int i = 0; i < used[0]; i++) if (sum - prefix[i] == target) count++;
        prefix[used[0]++] = sum;
        count += countPathSums(node.left, sum, target, prefix, used);
        count += countPathSums(node.right, sum, target, prefix, used);
        used[0]--;
        return count;
    }

    private static long leafGain(Node node, long[] best) {
        if (node.left == null && node.right == null) return node.value;
        long left = node.left == null ? Long.MIN_VALUE : leafGain(node.left, best);
        long right = node.right == null ? Long.MIN_VALUE : leafGain(node.right, best);
        if (node.left != null && node.right != null) best[0] = Math.max(best[0], left + right + node.value);
        return node.value + Math.max(left, right);
    }

    private static int[] horizontalView(Node root, boolean top) {
        if (root == null) return new int[0];
        int min = minHorizontal(root, 0), max = maxHorizontal(root, 0);
        int width = max - min + 1;
        int[] values = new int[width], depths = new int[width];
        boolean[] occupied = new boolean[width];
        Node[] nodes = new Node[size(root)];
        int[] columns = new int[nodes.length], levels = new int[nodes.length];
        int head = 0, tail = 0;
        nodes[tail] = root; columns[tail] = -min; levels[tail++] = 0;
        while (head < tail) {
            Node node = nodes[head];
            int column = columns[head], level = levels[head++];
            if (!occupied[column] || (top ? level < depths[column] : level >= depths[column])) {
                values[column] = node.value;
                depths[column] = level;
                occupied[column] = true;
            }
            if (node.left != null) { nodes[tail] = node.left; columns[tail] = column - 1; levels[tail++] = level + 1; }
            if (node.right != null) { nodes[tail] = node.right; columns[tail] = column + 1; levels[tail++] = level + 1; }
        }
        return values;
    }

    private static boolean validBst(Node node, long minimum, long maximum) {
        if (node == null) return true;
        if (node.value <= minimum || node.value >= maximum) return false;
        return validBst(node.left, minimum, node.value) && validBst(node.right, node.value, maximum);
    }

    private static int indexOf(int[] values, int left, int right, int target) {
        for (int i = left; i <= right; i++) if (values[i] == target) return i;
        return -1;
    }

    private static void validateTraversals(int[] inorder, int[] traversal) {
        if (inorder == null || traversal == null || inorder.length != traversal.length) {
            throw new IllegalArgumentException("Traversal arrays must be non-null and have equal lengths");
        }
    }

    private static int[] copyOf(int[] values, int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) result[i] = values[i];
        return result;
    }

    private static double[] copyOf(double[] values, int length) {
        double[] result = new double[length];
        for (int i = 0; i < length; i++) result[i] = values[i];
        return result;
    }

    private static void reverse(int[] values) { reverse(values, 0, values.length - 1); }
    private static void reverse(int[] values, int left, int right) {
        while (left < right) {
            int temporary = values[left]; values[left++] = values[right]; values[right--] = temporary;
        }
    }

    private static final class DiameterHeight {
        final int height;
        final int diameter;
        DiameterHeight(int height, int diameter) { this.height = height; this.diameter = diameter; }
    }
}
