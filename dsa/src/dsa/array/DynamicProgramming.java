package dsa.array;

public final class DynamicProgramming {
    private DynamicProgramming() { }

    public static long fibonacci(int n) {
        if (n < 0) throw new IllegalArgumentException("n cannot be negative");
        if (n > 92) throw new ArithmeticException("Fibonacci result exceeds the long range");
        if (n < 2) return n;
        long previous = 0, current = 1;
        for (int i = 2; i <= n; i++) {
            long next = previous + current;
            previous = current;
            current = next;
        }
        return current;
    }

    public static int longestCommonSubsequenceLength(String first, String second) {
        int[] previous = new int[second.length() + 1];
        int[] current = new int[second.length() + 1];
        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                current[j] = first.charAt(i - 1) == second.charAt(j - 1)
                    ? previous[j - 1] + 1
                    : Math.max(previous[j], current[j - 1]);
            }
            int[] temporary = previous;
            previous = current;
            current = temporary;
            for (int j = 0; j < current.length; j++) current[j] = 0;
        }
        return previous[second.length()];
    }

    public static int longestIncreasingSubsequenceLength(int[] values) {
        int[] tails = new int[values.length];
        int length = 0;
        for (int value : values) {
            int low = 0, high = length;
            while (low < high) {
                int middle = low + (high - low) / 2;
                if (tails[middle] < value) low = middle + 1;
                else high = middle;
            }
            tails[low] = value;
            if (low == length) length++;
        }
        return length;
    }

    public static int zeroOneKnapsack(int[] weights, int[] values, int capacity) {
        if (weights.length != values.length) throw new IllegalArgumentException("Weights and values must have equal lengths");
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
        int[] best = new int[capacity + 1];
        for (int item = 0; item < weights.length; item++) {
            if (weights[item] < 0) throw new IllegalArgumentException("Weights cannot be negative");
            for (int current = capacity; current >= weights[item]; current--) {
                best[current] = Math.max(best[current], best[current - weights[item]] + values[item]);
            }
        }
        return best[capacity];
    }

    public static int minimumCoinCount(int[] coins, int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        int[] best = new int[amount + 1];
        for (int i = 1; i <= amount; i++) best[i] = amount + 1;
        for (int coin : coins) {
            if (coin <= 0) throw new IllegalArgumentException("Coin values must be positive");
            for (int value = coin; value <= amount; value++) {
                best[value] = Math.min(best[value], best[value - coin] + 1);
            }
        }
        return best[amount] > amount ? -1 : best[amount];
    }
}
