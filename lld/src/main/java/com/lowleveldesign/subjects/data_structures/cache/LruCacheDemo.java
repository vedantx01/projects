package com.lowleveldesign.subjects.data_structures.cache;

public final class LruCacheDemo {
    private LruCacheDemo() {
    }

    public static void run() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("one", 1);
        cache.put("two", 2);
        cache.get("one");
        cache.put("three", 3);
        System.out.println("LRU cache: one=" + cache.get("one") + ", two=" + cache.get("two"));
    }
}
