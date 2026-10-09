package model.adt;

import model.exceptions.ADTException;

import java.util.Map;

public interface MyIDictionary<K, V> {
    void put(K key, V value) throws ADTException;
    boolean isDefined(K key);
    V lookup(K key) throws ADTException;
    void update(K key, V value) throws ADTException;
    Map<K, V> getContent();
    String toString();
}
