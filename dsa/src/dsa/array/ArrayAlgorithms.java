package dsa.array;

public final class ArrayAlgorithms {
    private ArrayAlgorithms() { }

    private static final class LongIntHashMap {
        private long[] keys;
        private int[] values;
        private byte[] states;
        private int size;
        LongIntHashMap(int expectedSize) {
            int capacity = 2;
            while (capacity < expectedSize * 2L && capacity < (1 << 30)) capacity <<= 1;
            keys = new long[capacity];
            values = new int[capacity];
            states = new byte[capacity];
        }
        boolean containsKey(long key) { return find(key) >= 0; }
        int getOrDefault(long key, int fallback) {
            int index = find(key);
            return index < 0 ? fallback : values[index];
        }
        void put(long key, int value) {
            if ((size + 1) * 10L >= keys.length * 7L) resize();
            int index = (int) mix(key) & (keys.length - 1);
            while (states[index] == 1 && keys[index] != key) index = (index + 1) & (keys.length - 1);
            if (states[index] != 1) size++;
            states[index] = 1;
            keys[index] = key;
            values[index] = value;
        }
        private int find(long key) {
            int index = (int) mix(key) & (keys.length - 1);
            for (int probes = 0; probes < keys.length; probes++) {
                if (states[index] == 0) return -1;
                if (states[index] == 1 && keys[index] == key) return index;
                index = (index + 1) & (keys.length - 1);
            }
            return -1;
        }
        private void resize() {
            long[] oldKeys = keys;
            int[] oldValues = values;
            byte[] oldStates = states;
            if (keys.length >= (1 << 30)) throw new IllegalStateException("Prefix map is too large");
            keys = new long[keys.length * 2];
            values = new int[keys.length];
            states = new byte[keys.length];
            size = 0;
            for (int i = 0; i < oldKeys.length; i++) if (oldStates[i] == 1) put(oldKeys[i], oldValues[i]);
        }
        private static long mix(long value) {
            value ^= value >>> 33;
            value *= 0xff51afd7ed558ccdL;
            value ^= value >>> 33;
            value *= 0xc4ceb9fe1a85ec53L;
            return value ^ (value >>> 33);
        }
    }

    public static void traverse(int[] values) {
        for (int value : values) System.out.print(value + " ");
        System.out.println();
    }

    public static int[] insert(int[] values, int index, int value) {
        if (index < 0 || index > values.length) throw new IndexOutOfBoundsException("Index: " + index);
        int[] result = new int[values.length + 1];
        copy(values, 0, result, 0, index);
        result[index] = value;
        copy(values, index, result, index + 1, values.length - index);
        return result;
    }

    public static int[] deleteAt(int[] values, int index) {
        checkIndex(values, index);
        int[] result = new int[values.length - 1];
        copy(values, 0, result, 0, index);
        copy(values, index + 1, result, index, values.length - index - 1);
        return result;
    }

    public static int linearSearch(int[] values, int target) {
        for (int i = 0; i < values.length; i++) if (values[i] == target) return i;
        return -1;
    }

    public static void update(int[] values, int index, int value) {
        checkIndex(values, index);
        values[index] = value;
    }

    public static int[] minMax(int[] values) {
        if (values.length == 0) throw new IllegalArgumentException("Array cannot be empty");
        int minimum = values[0], maximum = values[0];
        for (int value : values) {
            if (value < minimum) minimum = value;
            if (value > maximum) maximum = value;
        }
        return new int[] {minimum, maximum};
    }

    public static void reverse(int[] values) { reverse(values, 0, values.length - 1); }

    public static void rotateLeft(int[] values, int distance) {
        if (values.length == 0) return;
        int shift = Math.floorMod(distance, values.length);
        reverse(values, 0, shift - 1);
        reverse(values, shift, values.length - 1);
        reverse(values);
    }

    public static void rotateRight(int[] values, int distance) {
        if (values.length == 0) return;
        rotateLeft(values, -Math.floorMod(distance, values.length));
    }

    public static int[][] frequencyCount(int[] values) {
        HashTableInt counts = new HashTableInt(Math.max(2, values.length * 2));
        for (int value : values) counts.put(value, counts.getOrDefault(value, 0) + 1);
        int[][] result = new int[counts.size()][2];
        int index = 0;
        for (int value : values) {
            if (!counts.containsKey(value)) continue;
            int count = counts.get(value);
            result[index][0] = value;
            result[index++][1] = count;
            counts.remove(value);
        }
        return result;
    }

    public static int[] removeDuplicates(int[] values) {
        HashTableInt seen = new HashTableInt(Math.max(2, values.length * 2));
        int[] result = new int[values.length];
        int size = 0;
        for (int value : values) {
            if (!seen.containsKey(value)) {
                seen.put(value, 1);
                result[size++] = value;
            }
        }
        return copyOf(result, size);
    }

    public static long[] prefixSums(int[] values) {
        long[] prefix = new long[values.length];
        long sum = 0;
        for (int i = 0; i < values.length; i++) prefix[i] = sum += values[i];
        return prefix;
    }

    public static long rangeSum(long[] prefix, int left, int right) {
        if (left < 0 || right < left || right >= prefix.length) throw new IndexOutOfBoundsException("Invalid range");
        return prefix[right] - (left == 0 ? 0 : prefix[left - 1]);
    }

    public static int[] differenceArray(int[] values) {
        int[] difference = new int[values.length];
        if (values.length > 0) {
            difference[0] = values[0];
            for (int i = 1; i < values.length; i++) difference[i] = values[i] - values[i - 1];
        }
        return difference;
    }

    public static void addRange(int[] difference, int left, int right, int delta) {
        if (left < 0 || right < left || right >= difference.length) throw new IndexOutOfBoundsException("Invalid range");
        difference[left] += delta;
        if (right + 1 < difference.length) difference[right + 1] -= delta;
    }

    public static int[] restoreFromDifference(int[] difference) {
        int[] result = new int[difference.length];
        int sum = 0;
        for (int i = 0; i < difference.length; i++) result[i] = sum += difference[i];
        return result;
    }

    public static int equilibriumIndex(int[] values) {
        long total = 0, left = 0;
        for (int value : values) total += value;
        for (int i = 0; i < values.length; i++) {
            if (left == total - left - values[i]) return i;
            left += values[i];
        }
        return -1;
    }

    public static long[] productExceptSelf(int[] values) {
        long[] result = new long[values.length];
        long prefix = 1;
        for (int i = 0; i < values.length; i++) {
            result[i] = prefix;
            prefix *= values[i];
        }
        long suffix = 1;
        for (int i = values.length - 1; i >= 0; i--) {
            result[i] *= suffix;
            suffix *= values[i];
        }
        return result;
    }

    public static int countSubarraysWithSum(int[] values, int target) {
        LongIntHashMap frequencies = new LongIntHashMap(values.length);
        frequencies.put(0, 1);
        long prefix = 0, count = 0;
        for (int value : values) {
            prefix += value;
            count += frequencies.getOrDefault(prefix - target, 0);
            frequencies.put(prefix, frequencies.getOrDefault(prefix, 0) + 1);
        }
        if (count > Integer.MAX_VALUE) throw new ArithmeticException("Subarray count exceeds integer range");
        return (int) count;
    }

    public static long maximumSubarraySum(int[] values) {
        if (values.length == 0) throw new IllegalArgumentException("Array cannot be empty");
        long best = values[0], current = values[0];
        for (int i = 1; i < values.length; i++) {
            current = Math.max(values[i], current + values[i]);
            best = Math.max(best, current);
        }
        return best;
    }

    public static long maximumProductSubarray(int[] values) {
        if (values.length == 0) throw new IllegalArgumentException("Array cannot be empty");
        long maximum = values[0], minimum = values[0], best = values[0];
        for (int i = 1; i < values.length; i++) {
            long value = values[i];
            if (value < 0) {
                long temporary = maximum;
                maximum = minimum;
                minimum = temporary;
            }
            maximum = Math.max(value, maximum * value);
            minimum = Math.min(value, minimum * value);
            best = Math.max(best, maximum);
        }
        return best;
    }

    public static int[] runningSum(int[] values) {
        int[] result = new int[values.length];
        int sum = 0;
        for (int i = 0; i < values.length; i++) result[i] = sum += values[i];
        return result;
    }

    public static int[] twoSumSorted(int[] sorted, int target) {
        int left = 0, right = sorted.length - 1;
        while (left < right) {
            long sum = (long) sorted[left] + sorted[right];
            if (sum == target) return new int[] {left, right};
            if (sum < target) left++;
            else right--;
        }
        return new int[0];
    }

    public static int[] twoSum(int[] values, int target) {
        HashTableInt indices = new HashTableInt(Math.max(2, values.length * 2));
        for (int i = 0; i < values.length; i++) {
            long complement = (long) target - values[i];
            if (complement >= Integer.MIN_VALUE && complement <= Integer.MAX_VALUE
                && indices.containsKey((int) complement)) {
                return new int[] {indices.get((int) complement), i};
            }
            if (!indices.containsKey(values[i])) indices.put(values[i], i);
        }
        return new int[0];
    }

    public static int[][] threeSum(int[] values, int target) {
        int[] sorted = copyOf(values, values.length);
        SortingAlgorithms.quickSort(sorted);
        int[][] triples = new int[Math.max(1, values.length)][3];
        int count = 0;
        for (int i = 0; i < sorted.length - 2; i++) {
            if (i > 0 && sorted[i] == sorted[i - 1]) continue;
            int left = i + 1, right = sorted.length - 1;
            while (left < right) {
                long sum = (long) sorted[i] + sorted[left] + sorted[right];
                if (sum == target) {
                    if (count == triples.length) triples = growTriples(triples);
                    triples[count++] = new int[] {sorted[i], sorted[left], sorted[right]};
                    int leftValue = sorted[left], rightValue = sorted[right];
                    while (left < right && sorted[left] == leftValue) left++;
                    while (left < right && sorted[right] == rightValue) right--;
                } else if (sum < target) left++;
                else right--;
            }
        }
        return copyTriples(triples, count);
    }

    public static int[][] fourSum(int[] values, int target) {
        int[] sorted = copyOf(values, values.length);
        SortingAlgorithms.quickSort(sorted);
        int[][] result = new int[Math.max(1, values.length)][4];
        int count = 0;
        for (int i = 0; i < sorted.length - 3; i++) {
            if (i > 0 && sorted[i] == sorted[i - 1]) continue;
            for (int j = i + 1; j < sorted.length - 2; j++) {
                if (j > i + 1 && sorted[j] == sorted[j - 1]) continue;
                int left = j + 1, right = sorted.length - 1;
                while (left < right) {
                    long sum = (long) sorted[i] + sorted[j] + sorted[left] + sorted[right];
                    if (sum == target) {
                        if (count == result.length) result = growQuads(result);
                        result[count++] = new int[] {sorted[i], sorted[j], sorted[left], sorted[right]};
                        int lv = sorted[left], rv = sorted[right];
                        while (left < right && sorted[left] == lv) left++;
                        while (left < right && sorted[right] == rv) right--;
                    } else if (sum < target) left++;
                    else right--;
                }
            }
        }
        return copyQuads(result, count);
    }

    public static boolean hasPairWithDifference(int[] values, int difference) {
        long target = Math.abs((long) difference);
        int[] sorted = copyOf(values, values.length);
        SortingAlgorithms.quickSort(sorted);
        int left = 0, right = 1;
        while (right < sorted.length) {
            if (left == right) { right++; continue; }
            long current = (long) sorted[right] - sorted[left];
            if (current == target) return true;
            if (current < target) right++;
            else left++;
        }
        return false;
    }

    public static long maxContainerArea(int[] heights) {
        int left = 0, right = heights.length - 1;
        long best = 0;
        while (left < right) {
            if (heights[left] < 0 || heights[right] < 0) throw new IllegalArgumentException("Heights cannot be negative");
            best = Math.max(best, (long) Math.min(heights[left], heights[right]) * (right - left));
            if (heights[left] <= heights[right]) left++;
            else right--;
        }
        return best;
    }

    public static long trappedRainWater(int[] heights) {
        int left = 0, right = heights.length - 1, leftMax = 0, rightMax = 0;
        long water = 0;
        while (left <= right) {
            if (heights[left] < 0 || heights[right] < 0) throw new IllegalArgumentException("Heights cannot be negative");
            if (heights[left] <= heights[right]) {
                leftMax = Math.max(leftMax, heights[left]);
                water += leftMax - heights[left++];
            } else {
                rightMax = Math.max(rightMax, heights[right]);
                water += rightMax - heights[right--];
            }
        }
        return water;
    }

    public static int[] slidingWindowMaximum(int[] values, int window) {
        if (window <= 0 || window > values.length) throw new IllegalArgumentException("Invalid window size");
        int[] deque = new int[values.length], result = new int[values.length - window + 1];
        int head = 0, tail = 0;
        for (int i = 0; i < values.length; i++) {
            while (head < tail && deque[head] <= i - window) head++;
            while (head < tail && values[deque[tail - 1]] <= values[i]) tail--;
            deque[tail++] = i;
            if (i >= window - 1) result[i - window + 1] = values[deque[head]];
        }
        return result;
    }

    public static int longestSubarrayWithSum(int[] values, int target) {
        LongIntHashMap earliest = new LongIntHashMap(values.length);
        long prefix = 0;
        int longest = 0;
        earliest.put(0, -1);
        for (int i = 0; i < values.length; i++) {
            prefix += values[i];
            long prior = prefix - target;
            if (earliest.containsKey(prior)) longest = Math.max(longest, i - earliest.getOrDefault(prior, -1));
            if (!earliest.containsKey(prefix)) earliest.put(prefix, i);
        }
        return longest;
    }

    public static int minimumSubarrayLengthAtLeast(int[] positiveValues, int target) {
        int left = 0, best = Integer.MAX_VALUE;
        long sum = 0;
        for (int right = 0; right < positiveValues.length; right++) {
            if (positiveValues[right] <= 0) throw new IllegalArgumentException("Values must be positive");
            sum += positiveValues[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= positiveValues[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    public static int longestUniqueSubarray(int[] values) {
        HashTableInt lastSeen = new HashTableInt(Math.max(2, values.length * 2));
        int left = 0, best = 0;
        for (int right = 0; right < values.length; right++) {
            if (lastSeen.containsKey(values[right])) left = Math.max(left, lastSeen.get(values[right]) + 1);
            lastSeen.put(values[right], right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void sort012(int[] values) {
        int low = 0, middle = 0, high = values.length - 1;
        while (middle <= high) {
            if (values[middle] == 0) swap(values, low++, middle++);
            else if (values[middle] == 1) middle++;
            else if (values[middle] == 2) swap(values, middle, high--);
            else throw new IllegalArgumentException("Array values must be 0, 1, or 2");
        }
    }

    public static int partition(int[] values, int pivot) {
        int boundary = 0;
        for (int i = 0; i < values.length; i++) if (values[i] < pivot) swap(values, boundary++, i);
        return boundary;
    }

    public static void mergeSortedIntoFirst(int[] first, int firstCount, int[] second) {
        if (firstCount < 0 || firstCount > first.length || first.length - firstCount != second.length) {
            throw new IllegalArgumentException("First array must have enough trailing capacity");
        }
        int i = firstCount - 1, j = second.length - 1, write = first.length - 1;
        while (j >= 0) first[write--] = i >= 0 && first[i] > second[j] ? first[i--] : second[j--];
    }

    public static int[] intersection(int[] first, int[] second) {
        int[] a = removeDuplicates(first), b = removeDuplicates(second), result = new int[Math.min(a.length, b.length)];
        int count = 0;
        for (int value : a) if (linearSearch(b, value) >= 0) result[count++] = value;
        return copyOf(result, count);
    }

    public static int[] union(int[] first, int[] second) {
        int[] result = new int[first.length + second.length];
        int count = 0;
        for (int value : first) if (linearSearch(result, count, value) < 0) result[count++] = value;
        for (int value : second) if (linearSearch(result, count, value) < 0) result[count++] = value;
        return copyOf(result, count);
    }

    public static int kthSmallest(int[] values, int k) { return quickSelect(values, k, false); }
    public static int kthLargest(int[] values, int k) { return quickSelect(values, k, true); }

    public static int majorityElement(int[] values) {
        int candidate = 0, count = 0;
        for (int value : values) {
            if (count == 0) candidate = value;
            count += value == candidate ? 1 : -1;
        }
        count = 0;
        for (int value : values) if (value == candidate) count++;
        return count > values.length / 2 ? candidate : -1;
    }

    public static long countInversions(int[] values) {
        return countInversions(values, new int[values.length], 0, values.length);
    }

    public static void rearrangePositiveNegative(int[] values) {
        int[] temporary = new int[values.length];
        int index = 0;
        for (int value : values) if (value < 0) temporary[index++] = value;
        for (int value : values) if (value >= 0) temporary[index++] = value;
        copy(temporary, 0, values, 0, values.length);
    }

    public static void waveArray(int[] values) {
        for (int i = 0; i + 1 < values.length; i += 2) {
            if (values[i] < values[i + 1]) swap(values, i, i + 1);
            if (i + 2 < values.length && values[i + 1] < values[i + 2]) swap(values, i + 1, i + 2);
        }
    }

    public static int[][] allSubarrays(int[] values) {
        long count = (long) values.length * (values.length + 1) / 2;
        if (count > Integer.MAX_VALUE) throw new IllegalArgumentException("Too many subarrays to materialize");
        int[][] result = new int[(int) count][];
        int output = 0;
        for (int left = 0; left < values.length; left++) {
            int[] current = new int[values.length - left];
            for (int right = left; right < values.length; right++) {
                current[right - left] = values[right];
                result[output++] = copyOf(current, right - left + 1);
            }
        }
        return result;
    }

    public static boolean hasZeroSumSubarray(int[] values) {
        LongIntHashMap seen = new LongIntHashMap(values.length);
        seen.put(0, 1);
        long sum = 0;
        for (int value : values) {
            sum += value;
            if (seen.containsKey(sum)) return true;
            seen.put(sum, 1);
        }
        return false;
    }

    public static int countSubarraysDivisibleByK(int[] values, int k) {
        if (k == 0) throw new IllegalArgumentException("Divisor cannot be zero");
        HashTableInt frequencies = new HashTableInt(Math.max(2, values.length * 2));
        frequencies.put(0, 1);
        long prefix = 0, count = 0;
        long divisor = Math.abs((long) k);
        for (int value : values) {
            prefix += value;
            int remainder = (int) Math.floorMod(prefix, divisor);
            count += frequencies.getOrDefault(remainder, 0);
            frequencies.put(remainder, frequencies.getOrDefault(remainder, 0) + 1);
        }
        if (count > Integer.MAX_VALUE) throw new ArithmeticException("Subarray count exceeds integer range");
        return (int) count;
    }

    public static int longestIncreasingContiguousSubarray(int[] values) {
        if (values.length == 0) return 0;
        int current = 1, best = 1;
        for (int i = 1; i < values.length; i++) {
            current = values[i] > values[i - 1] ? current + 1 : 1;
            best = Math.max(best, current);
        }
        return best;
    }

    public static int longestDecreasingContiguousSubarray(int[] values) {
        if (values.length == 0) return 0;
        int current = 1, best = 1;
        for (int i = 1; i < values.length; i++) {
            current = values[i] < values[i - 1] ? current + 1 : 1;
            best = Math.max(best, current);
        }
        return best;
    }

    public static int longestAlternatingSubarray(int[] values) {
        if (values.length == 0) return 0;
        int current = 1, best = 1;
        for (int i = 1; i < values.length; i++) {
            if ((values[i] & 1) != (values[i - 1] & 1)) current++;
            else current = 1;
            best = Math.max(best, current);
        }
        return best;
    }

    public static long maximumCircularSubarraySum(int[] values) {
        if (values.length == 0) throw new IllegalArgumentException("Array cannot be empty");
        long total = 0, maxEnding = values[0], maxSum = values[0], minEnding = values[0], minSum = values[0];
        total = values[0];
        for (int i = 1; i < values.length; i++) {
            total += values[i];
            maxEnding = Math.max(values[i], maxEnding + values[i]);
            maxSum = Math.max(maxSum, maxEnding);
            minEnding = Math.min(values[i], minEnding + values[i]);
            minSum = Math.min(minSum, minEnding);
        }
        return maxSum < 0 ? maxSum : Math.max(maxSum, total - minSum);
    }

    public static long maximumDifferenceLaterMinusEarlier(int[] values) {
        if (values.length < 2) throw new IllegalArgumentException("At least two values are required");
        int minimum = values[0];
        long best = (long) values[1] - values[0];
        for (int i = 1; i < values.length; i++) {
            best = Math.max(best, (long) values[i] - minimum);
            minimum = Math.min(minimum, values[i]);
        }
        return best;
    }

    public static long maximumStockProfitOneTransaction(int[] prices) {
        if (prices.length < 2) return 0;
        int minimum = prices[0];
        long best = 0;
        for (int i = 1; i < prices.length; i++) {
            best = Math.max(best, (long) prices[i] - minimum);
            minimum = Math.min(minimum, prices[i]);
        }
        return best;
    }

    public static long maximumStockProfitUnlimitedTransactions(int[] prices) {
        long profit = 0;
        for (int i = 1; i < prices.length; i++) if (prices[i] > prices[i - 1]) profit += (long) prices[i] - prices[i - 1];
        return profit;
    }

    private static int quickSelect(int[] values, int k, boolean largest) {
        if (k < 1 || k > values.length) throw new IllegalArgumentException("k is out of range");
        int target = largest ? values.length - k : k - 1;
        int low = 0, high = values.length - 1;
        while (low <= high) {
            int pivot = values[low + (high - low) / 2], less = low, current = low, greater = high;
            while (current <= greater) {
                if (values[current] < pivot) swap(values, less++, current++);
                else if (values[current] > pivot) swap(values, current, greater--);
                else current++;
            }
            if (target < less) high = less - 1;
            else if (target > greater) low = greater + 1;
            else return values[target];
        }
        throw new AssertionError("Quickselect target was not found");
    }

    private static long countInversions(int[] values, int[] temporary, int left, int right) {
        if (right - left < 2) return 0;
        int middle = left + (right - left) / 2;
        long count = countInversions(values, temporary, left, middle)
            + countInversions(values, temporary, middle, right);
        int first = left, second = middle, output = left;
        while (first < middle && second < right) {
            if (values[first] <= values[second]) temporary[output++] = values[first++];
            else {
                temporary[output++] = values[second++];
                count += middle - first;
            }
        }
        while (first < middle) temporary[output++] = values[first++];
        while (second < right) temporary[output++] = values[second++];
        copy(temporary, left, values, left, right - left);
        return count;
    }

    private static void reverse(int[] values, int left, int right) {
        while (left < right) swap(values, left++, right--);
    }

    private static int[][] growTriples(int[][] values) {
        int[][] expanded = new int[Math.max(1, values.length * 2)][3];
        for (int i = 0; i < values.length; i++) expanded[i] = values[i];
        return expanded;
    }

    private static int[][] growQuads(int[][] values) {
        int[][] expanded = new int[values.length * 2][4];
        for (int i = 0; i < values.length; i++) expanded[i] = values[i];
        return expanded;
    }

    private static int[][] copyTriples(int[][] values, int count) {
        int[][] result = new int[count][];
        for (int i = 0; i < count; i++) result[i] = values[i];
        return result;
    }

    private static int[][] copyQuads(int[][] values, int count) {
        int[][] result = new int[count][];
        for (int i = 0; i < count; i++) result[i] = values[i];
        return result;
    }

    private static int linearSearch(int[] values, int length, int target) {
        for (int i = 0; i < length; i++) if (values[i] == target) return i;
        return -1;
    }

    private static int[] copyOf(int[] values, int length) {
        int[] result = new int[length];
        copy(values, 0, result, 0, length);
        return result;
    }

    private static void copy(int[] source, int sourceIndex, int[] destination, int destinationIndex, int length) {
        for (int i = 0; i < length; i++) destination[destinationIndex + i] = source[sourceIndex + i];
    }

    private static void checkIndex(int[] values, int index) {
        if (index < 0 || index >= values.length) throw new IndexOutOfBoundsException("Index: " + index);
    }

    private static void swap(int[] values, int first, int second) {
        int temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }
}
