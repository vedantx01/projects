package dsa.array;

public final class SearchingAlgorithms {
    private SearchingAlgorithms() { }

    public static int linearSearch(int[] values, int target) {
        for (int i = 0; i < values.length; i++) if (values[i] == target) return i;
        return -1;
    }

    public static int binarySearch(int[] sorted, int target) {
        int low = 0, high = sorted.length - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            if (sorted[middle] == target) return middle;
            if (sorted[middle] < target) low = middle + 1;
            else high = middle - 1;
        }
        return -1;
    }

    public static int lowerBound(int[] sorted, int target) {
        int low = 0, high = sorted.length;
        while (low < high) {
            int middle = low + (high - low) / 2;
            if (sorted[middle] < target) low = middle + 1;
            else high = middle;
        }
        return low;
    }

    public static int upperBound(int[] sorted, int target) {
        int low = 0, high = sorted.length;
        while (low < high) {
            int middle = low + (high - low) / 2;
            if (sorted[middle] <= target) low = middle + 1;
            else high = middle;
        }
        return low;
    }

    public static int searchRotatedSorted(int[] values, int target) {
        int low = 0, high = values.length - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            if (values[middle] == target) return middle;
            if (values[low] <= values[middle]) {
                if (values[low] <= target && target < values[middle]) high = middle - 1;
                else low = middle + 1;
            } else {
                if (values[middle] < target && target <= values[high]) low = middle + 1;
                else high = middle - 1;
            }
        }
        return -1;
    }

    public static int jumpSearch(int[] sorted, int target) {
        if (sorted.length == 0) return -1;
        int step = Math.max(1, (int) Math.sqrt(sorted.length));
        int start = 0, end = step;
        while (start < sorted.length && sorted[Math.min(end, sorted.length) - 1] < target) {
            start = end;
            end += step;
        }
        for (int i = start; i < Math.min(end, sorted.length); i++) {
            if (sorted[i] == target) return i;
            if (sorted[i] > target) return -1;
        }
        return -1;
    }
}
