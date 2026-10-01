package dsa.array;

public final class BacktrackingAlgorithms {
    public interface PermutationVisitor {
        void visit(String permutation);
    }

    private BacktrackingAlgorithms() { }

    public static void forEachPermutation(String value, PermutationVisitor visitor) {
        if (value == null || visitor == null) throw new IllegalArgumentException("Arguments cannot be null");
        char[] characters = value.toCharArray();
        permute(characters, 0, visitor);
    }

    public static long countNQueensSolutions(int n) {
        if (n < 0 || n > 15) throw new IllegalArgumentException("n must be between 0 and 15");
        return placeQueen(new boolean[n], new boolean[Math.max(0, 2 * n - 1)],
            new boolean[Math.max(0, 2 * n - 1)], 0, n);
    }

    public static long countMazePaths(boolean[][] blocked) {
        if (blocked == null) throw new IllegalArgumentException("Maze cannot be null");
        if (blocked.length == 0) return 0;
        if (blocked[0] == null) throw new IllegalArgumentException("Maze rows cannot be null");
        int columns = blocked[0].length;
        for (boolean[] row : blocked) {
            if (row == null || row.length != columns) throw new IllegalArgumentException("Maze must be rectangular");
        }
        if (columns == 0 || blocked[0][0] || blocked[blocked.length - 1][columns - 1]) return 0;
        return countPaths(blocked, new boolean[blocked.length][columns], 0, 0);
    }

    public static boolean subsetSumExists(int[] values, int target) {
        return subsetSum(values, 0, target);
    }

    private static void permute(char[] values, int position, PermutationVisitor visitor) {
        if (position == values.length) {
            visitor.visit(new String(values));
            return;
        }
        for (int i = position; i < values.length; i++) {
            swap(values, position, i);
            permute(values, position + 1, visitor);
            swap(values, position, i);
        }
    }

    private static long placeQueen(boolean[] columns, boolean[] descending, boolean[] ascending, int row, int n) {
        if (row == n) return 1;
        long solutions = 0;
        for (int column = 0; column < n; column++) {
            int down = row - column + n - 1, up = row + column;
            if (columns[column] || descending[down] || ascending[up]) continue;
            columns[column] = descending[down] = ascending[up] = true;
            solutions += placeQueen(columns, descending, ascending, row + 1, n);
            columns[column] = descending[down] = ascending[up] = false;
        }
        return solutions;
    }

    private static long countPaths(boolean[][] blocked, boolean[][] visited, int row, int column) {
        if (row == blocked.length - 1 && column == blocked[0].length - 1) return 1;
        visited[row][column] = true;
        long paths = 0;
        if (row + 1 < blocked.length && !blocked[row + 1][column] && !visited[row + 1][column]) {
            paths += countPaths(blocked, visited, row + 1, column);
        }
        if (column + 1 < blocked[0].length && !blocked[row][column + 1] && !visited[row][column + 1]) {
            paths += countPaths(blocked, visited, row, column + 1);
        }
        visited[row][column] = false;
        return paths;
    }

    private static boolean subsetSum(int[] values, int index, int remaining) {
        if (remaining == 0) return true;
        if (index == values.length) return false;
        return subsetSum(values, index + 1, remaining)
            || subsetSum(values, index + 1, remaining - values[index]);
    }

    private static void swap(char[] values, int first, int second) {
        char temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }
}
