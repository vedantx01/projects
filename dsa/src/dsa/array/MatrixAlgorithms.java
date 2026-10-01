package dsa.array;

public final class MatrixAlgorithms {
    private MatrixAlgorithms() { }

    public static int[] traverse(int[][] matrix) {
        int columns = validateRectangular(matrix);
        int[] result = new int[matrix.length * columns];
        int index = 0;
        for (int[] row : matrix) for (int value : row) result[index++] = value;
        return result;
    }

    public static int[] spiralTraversal(int[][] matrix) {
        int columns = validateRectangular(matrix);
        if (matrix.length == 0 || columns == 0) return new int[0];
        int[] result = new int[matrix.length * columns];
        int output = 0, top = 0, bottom = matrix.length - 1, left = 0, right = columns - 1;
        while (top <= bottom && left <= right) {
            for (int column = left; column <= right; column++) result[output++] = matrix[top][column];
            top++;
            for (int row = top; row <= bottom; row++) result[output++] = matrix[row][right];
            right--;
            if (top <= bottom) {
                for (int column = right; column >= left; column--) result[output++] = matrix[bottom][column];
                bottom--;
            }
            if (left <= right) {
                for (int row = bottom; row >= top; row--) result[output++] = matrix[row][left];
                left++;
            }
        }
        return result;
    }

    public static void rotateClockwise90(int[][] matrix) {
        int columns = validateRectangular(matrix);
        if (matrix.length != columns) throw new IllegalArgumentException("In-place rotation requires a square matrix");
        for (int row = 0; row < matrix.length; row++) {
            for (int column = row + 1; column < columns; column++) {
                int temporary = matrix[row][column];
                matrix[row][column] = matrix[column][row];
                matrix[column][row] = temporary;
            }
        }
        for (int[] row : matrix) {
            for (int left = 0, right = row.length - 1; left < right; left++, right--) {
                int temporary = row[left];
                row[left] = row[right];
                row[right] = temporary;
            }
        }
    }

    public static int[][] transpose(int[][] matrix) {
        int columns = validateRectangular(matrix);
        int[][] transposed = new int[columns][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < columns; column++) transposed[column][row] = matrix[row][column];
        }
        return transposed;
    }

    public static int[] searchSortedMatrix(int[][] matrix, int target) {
        int columns = validateRectangular(matrix);
        if (matrix.length == 0 || columns == 0) return new int[0];
        int row = 0, column = columns - 1;
        while (row < matrix.length && column >= 0) {
            if (matrix[row][column] == target) return new int[] {row, column};
            if (matrix[row][column] > target) column--;
            else row++;
        }
        return new int[0];
    }

    public static long[][] prefixSum2D(int[][] matrix) {
        int columns = validateRectangular(matrix);
        long[][] prefix = new long[matrix.length + 1][columns + 1];
        for (int row = 1; row <= matrix.length; row++) {
            for (int column = 1; column <= columns; column++) {
                prefix[row][column] = matrix[row - 1][column - 1] + prefix[row - 1][column]
                    + prefix[row][column - 1] - prefix[row - 1][column - 1];
            }
        }
        return prefix;
    }

    public static long rectangleSum(long[][] prefix, int top, int left, int bottom, int right) {
        if (prefix == null || prefix.length == 0 || prefix[0] == null) {
            throw new IllegalArgumentException("Prefix table cannot be empty");
        }
        int rows = prefix.length - 1, columns = prefix[0].length - 1;
        if (top < 0 || left < 0 || bottom < top || right < left || bottom >= rows || right >= columns) {
            throw new IndexOutOfBoundsException("Invalid rectangle");
        }
        return prefix[bottom + 1][right + 1] - prefix[top][right + 1]
            - prefix[bottom + 1][left] + prefix[top][left];
    }

    private static int validateRectangular(int[][] matrix) {
        if (matrix == null) throw new IllegalArgumentException("Matrix cannot be null");
        if (matrix.length == 0) return 0;
        if (matrix[0] == null) throw new IllegalArgumentException("Matrix rows cannot be null");
        int columns = matrix[0].length;
        for (int[] row : matrix) {
            if (row == null || row.length != columns) throw new IllegalArgumentException("Matrix must be rectangular");
        }
        return columns;
    }
}
