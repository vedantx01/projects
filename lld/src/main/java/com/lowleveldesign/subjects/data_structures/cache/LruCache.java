package com.lowleveldesign.subjects.data_structures.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class LruCache<K, V> {
    private final Map<K, V> entries;

    public LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        entries = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(entries.get(key));
    }

    public void put(K key, V value) {
        entries.put(key, value);
    }

    public int size() {
        return entries.size();
    }
}
