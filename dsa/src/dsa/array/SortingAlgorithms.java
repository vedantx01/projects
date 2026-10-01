package dsa.array;

public final class SortingAlgorithms {
    private SortingAlgorithms() { }

    public static void bubbleSort(int[] values) {
        for (int end = values.length - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++) {
                if (values[i] > values[i + 1]) {
                    swap(values, i, i + 1);
                    changed = true;
                }
            }
            if (!changed) return;
        }
    }

    public static void selectionSort(int[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            int minimum = i;
            for (int j = i + 1; j < values.length; j++) {
                if (values[j] < values[minimum]) minimum = j;
            }
            swap(values, i, minimum);
        }
    }

    public static void insertionSort(int[] values) {
        for (int i = 1; i < values.length; i++) {
            int value = values[i], j = i - 1;
            while (j >= 0 && values[j] > value) {
                values[j + 1] = values[j--];
            }
            values[j + 1] = value;
        }
    }

    public static void shellSort(int[] values) {
        for (int gap = values.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < values.length; i++) {
                int value = values[i], j = i;
                while (j >= gap && values[j - gap] > value) {
                    values[j] = values[j - gap];
                    j -= gap;
                }
                values[j] = value;
            }
        }
    }

    public static void mergeSort(int[] values) {
        if (values.length < 2) return;
        mergeSort(values, new int[values.length], 0, values.length);
    }

    public static void quickSort(int[] values) {
        quickSort(values, 0, values.length - 1);
    }

    public static void heapSort(int[] values) { MinHeapInt.heapSort(values); }

    public static void countingSort(int[] values) {
        if (values.length < 2) return;
        int minimum = values[0], maximum = values[0];
        for (int value : values) {
            if (value < minimum) minimum = value;
            if (value > maximum) maximum = value;
        }
        long range = (long) maximum - minimum + 1;
        if (range > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Value range is too large for counting sort");
        }
        int[] counts = new int[(int) range];
        for (int value : values) counts[value - minimum]++;
        int index = 0;
        for (int i = 0; i < counts.length; i++) {
            while (counts[i]-- > 0) values[index++] = i + minimum;
        }
    }

    public static void radixSortNonNegative(int[] values) {
        int maximum = 0;
        for (int value : values) {
            if (value < 0) throw new IllegalArgumentException("Radix sort requires non-negative values");
            if (value > maximum) maximum = value;
        }
        int[] output = new int[values.length];
        for (long place = 1; maximum / place > 0; place *= 10) {
            int[] counts = new int[10];
            for (int value : values) counts[(int) (value / place % 10)]++;
            for (int i = 1; i < counts.length; i++) counts[i] += counts[i - 1];
            for (int i = values.length - 1; i >= 0; i--) {
                int digit = (int) (values[i] / place % 10);
                output[--counts[digit]] = values[i];
            }
            for (int i = 0; i < values.length; i++) values[i] = output[i];
        }
    }

    private static void mergeSort(int[] values, int[] temporary, int left, int right) {
        if (right - left < 2) return;
        int middle = left + (right - left) / 2;
        mergeSort(values, temporary, left, middle);
        mergeSort(values, temporary, middle, right);
        int first = left, second = middle, output = left;
        while (first < middle && second < right) {
            temporary[output++] = values[first] <= values[second] ? values[first++] : values[second++];
        }
        while (first < middle) temporary[output++] = values[first++];
        while (second < right) temporary[output++] = values[second++];
        for (int i = left; i < right; i++) values[i] = temporary[i];
    }

    private static void quickSort(int[] values, int low, int high) {
        while (low < high) {
            int pivot = values[low + (high - low) / 2];
            int less = low, current = low, greater = high;
            while (current <= greater) {
                if (values[current] < pivot) swap(values, less++, current++);
                else if (values[current] > pivot) swap(values, current, greater--);
                else current++;
            }
            if (less - low < high - greater) {
                quickSort(values, low, less - 1);
                low = greater + 1;
            } else {
                quickSort(values, greater + 1, high);
                high = less - 1;
            }
        }
    }

    private static void swap(int[] values, int first, int second) {
        int temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }
}
