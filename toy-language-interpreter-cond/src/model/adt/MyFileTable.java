package model.adt;

import java.util.HashMap;
import java.util.Map;

public class MyFileTable<K, V> implements MyIFileTable<K, V> {
    private final Map<K, V> content;

    public MyFileTable() {
        content = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        content.put(key, value);
    }

    @Override
    public V get(K key) {
        return content.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return content.containsKey(key);
    }

    @Override
    public void remove(K key) {
        content.remove(key);
    }

    @Override
    public Map<K, V> getContent() {
        return content;
    }

    @Override
    public void setContent(Map<K, V> newContent) {
        content.clear();
        content.putAll(newContent);
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
