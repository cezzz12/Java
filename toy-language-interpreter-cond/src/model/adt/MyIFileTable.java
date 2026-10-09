package model.adt;

import java.io.BufferedReader;
import java.util.Map;

public interface MyIFileTable<K, V> {
    void put(K key, V value);
    V get(K key);
    boolean containsKey(K key);
    void remove(K key);
    Map<K, V> getContent();
    void setContent(Map<K, V> newContent);
    String toString();
}
