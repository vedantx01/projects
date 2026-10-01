package dsa.graph;

public final class GraphGridAlgorithms {
    private static final int[] ROW_STEP = {-1, 1, 0, 0};
    private static final int[] COLUMN_STEP = {0, 0, -1, 1};
    private GraphGridAlgorithms() { }

    public static int numberOfIslands(char[][] grid) {
        int columns = validate(grid);
        if (grid.length == 0 || columns == 0) return 0;
        boolean[][] visited = new boolean[grid.length][columns];
        int[] queue = new int[grid.length * columns];
        int islands = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < columns; column++) {
                if (grid[row][column] != '1' || visited[row][column]) continue;
                islands++;
                int head = 0, tail = 0;
                queue[tail++] = row * columns + column;
                visited[row][column] = true;
                while (head < tail) {
                    int cell = queue[head++], currentRow = cell / columns, currentColumn = cell % columns;
                    for (int direction = 0; direction < 4; direction++) {
                        int nextRow = currentRow + ROW_STEP[direction];
                        int nextColumn = currentColumn + COLUMN_STEP[direction];
                        if (inside(nextRow, nextColumn, grid.length, columns)
                            && grid[nextRow][nextColumn] == '1' && !visited[nextRow][nextColumn]) {
                            visited[nextRow][nextColumn] = true;
                            queue[tail++] = nextRow * columns + nextColumn;
                        }
                    }
                }
            }
        }
        return islands;
    }

    public static int[][] floodFill(int[][] image, int startRow, int startColumn, int replacement) {
        int columns = validate(image);
        if (image.length == 0 || columns == 0) {
            if (startRow != 0 || startColumn != 0) throw new IndexOutOfBoundsException("Start cell is outside image");
            return image;
        }
        if (!inside(startRow, startColumn, image.length, columns)) {
            throw new IndexOutOfBoundsException("Start cell is outside image");
        }
        int original = image[startRow][startColumn];
        if (original == replacement) return image;
        int[] queue = new int[image.length * columns];
        int head = 0, tail = 0;
        queue[tail++] = startRow * columns + startColumn;
        image[startRow][startColumn] = replacement;
        while (head < tail) {
            int cell = queue[head++], row = cell / columns, column = cell % columns;
            for (int direction = 0; direction < 4; direction++) {
                int nextRow = row + ROW_STEP[direction], nextColumn = column + COLUMN_STEP[direction];
                if (inside(nextRow, nextColumn, image.length, columns) && image[nextRow][nextColumn] == original) {
                    image[nextRow][nextColumn] = replacement;
                    queue[tail++] = nextRow * columns + nextColumn;
                }
            }
        }
        return image;
    }

    public static int shortestPathBinaryMatrix(int[][] grid) {
        int columns = validate(grid), rows = grid.length;
        if (rows == 0 || columns == 0) return -1;
        if (grid[0][0] != 0 || grid[rows - 1][columns - 1] != 0) return -1;
        int[] queue = new int[rows * columns], distance = new int[rows * columns];
        for (int i = 0; i < distance.length; i++) distance[i] = -1;
        int head = 0, tail = 0, start = 0, destination = distance.length - 1;
        queue[tail++] = start;
        distance[start] = 1;
        while (head < tail) {
            int cell = queue[head++];
            if (cell == destination) return distance[cell];
            int row = cell / columns, column = cell % columns;
            for (int rowStep = -1; rowStep <= 1; rowStep++) {
                for (int columnStep = -1; columnStep <= 1; columnStep++) {
                    if (rowStep == 0 && columnStep == 0) continue;
                    int nextRow = row + rowStep, nextColumn = column + columnStep;
                    if (inside(nextRow, nextColumn, rows, columns) && grid[nextRow][nextColumn] == 0) {
                        int next = nextRow * columns + nextColumn;
                        if (distance[next] < 0) {
                            distance[next] = distance[cell] + 1;
                            queue[tail++] = next;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private static int validate(char[][] grid) {
        if (grid == null) throw new IllegalArgumentException("Grid cannot be null");
        if (grid.length == 0) return 0;
        if (grid[0] == null) throw new IllegalArgumentException("Grid rows cannot be null");
        int columns = grid[0].length;
        for (char[] row : grid) if (row == null || row.length != columns) throw new IllegalArgumentException("Grid must be rectangular");
        return columns;
    }

    private static int validate(int[][] grid) {
        if (grid == null) throw new IllegalArgumentException("Grid cannot be null");
        if (grid.length == 0) return 0;
        if (grid[0] == null) throw new IllegalArgumentException("Grid rows cannot be null");
        int columns = grid[0].length;
        for (int[] row : grid) if (row == null || row.length != columns) throw new IllegalArgumentException("Grid must be rectangular");
        return columns;
    }

    private static boolean inside(int row, int column, int rows, int columns) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
}
