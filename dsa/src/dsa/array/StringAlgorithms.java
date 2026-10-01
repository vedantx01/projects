package dsa.array;

public final class StringAlgorithms {
    private static final long MOD = 1_000_000_007L;
    private static final long BASE = 257L;
    private StringAlgorithms() { }

    public static int naiveSearch(String text, String pattern) {
        if (pattern.length() == 0) return 0;
        for (int start = 0; start + pattern.length() <= text.length(); start++) {
            int i = 0;
            while (i < pattern.length() && text.charAt(start + i) == pattern.charAt(i)) i++;
            if (i == pattern.length()) return start;
        }
        return -1;
    }

    public static int[] kmpSearchAll(String text, String pattern) {
        DynamicArrayInt matches = new DynamicArrayInt();
        if (pattern.length() == 0) {
            for (int i = 0; i <= text.length(); i++) matches.add(i);
            return matches.toArray();
        }
        int[] prefix = prefixFunction(pattern);
        for (int i = 0, matched = 0; i < text.length(); i++) {
            while (matched > 0 && text.charAt(i) != pattern.charAt(matched)) matched = prefix[matched - 1];
            if (text.charAt(i) == pattern.charAt(matched)) matched++;
            if (matched == pattern.length()) {
                matches.add(i - pattern.length() + 1);
                matched = prefix[matched - 1];
            }
        }
        return matches.toArray();
    }

    public static int rabinKarpSearch(String text, String pattern) {
        if (pattern.length() == 0) return 0;
        if (pattern.length() > text.length()) return -1;
        long patternHash = 0, windowHash = 0, power = 1;
        for (int i = 0; i < pattern.length(); i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
            if (i + 1 < pattern.length()) power = power * BASE % MOD;
        }
        for (int start = 0; start + pattern.length() <= text.length(); start++) {
            if (patternHash == windowHash && matchesAt(text, pattern, start)) return start;
            if (start + pattern.length() < text.length()) {
                windowHash = (windowHash - text.charAt(start) * power % MOD + MOD) % MOD;
                windowHash = (windowHash * BASE + text.charAt(start + pattern.length())) % MOD;
            }
        }
        return -1;
    }

    public static boolean isPalindrome(String value) {
        for (int left = 0, right = value.length() - 1; left < right; left++, right--) {
            if (value.charAt(left) != value.charAt(right)) return false;
        }
        return true;
    }

    public static boolean areAnagrams(String first, String second) {
        if (first.length() != second.length()) return false;
        int[] frequencies = new int[Character.MAX_VALUE + 1];
        for (int i = 0; i < first.length(); i++) {
            frequencies[first.charAt(i)]++;
            frequencies[second.charAt(i)]--;
        }
        for (int frequency : frequencies) if (frequency != 0) return false;
        return true;
    }

    public static int editDistance(String first, String second) {
        int[] previous = new int[second.length() + 1];
        int[] current = new int[second.length() + 1];
        for (int j = 0; j <= second.length(); j++) previous[j] = j;
        for (int i = 1; i <= first.length(); i++) {
            current[0] = i;
            for (int j = 1; j <= second.length(); j++) {
                int substitution = previous[j - 1] + (first.charAt(i - 1) == second.charAt(j - 1) ? 0 : 1);
                current[j] = Math.min(substitution, Math.min(previous[j] + 1, current[j - 1] + 1));
            }
            int[] temporary = previous;
            previous = current;
            current = temporary;
        }
        return previous[second.length()];
    }

    private static int[] prefixFunction(String pattern) {
        int[] prefix = new int[pattern.length()];
        for (int i = 1, matched = 0; i < pattern.length(); i++) {
            while (matched > 0 && pattern.charAt(i) != pattern.charAt(matched)) matched = prefix[matched - 1];
            if (pattern.charAt(i) == pattern.charAt(matched)) matched++;
            prefix[i] = matched;
        }
        return prefix;
    }

    private static boolean matchesAt(String text, String pattern, int start) {
        for (int i = 0; i < pattern.length(); i++) {
            if (text.charAt(start + i) != pattern.charAt(i)) return false;
        }
        return true;
    }
}
