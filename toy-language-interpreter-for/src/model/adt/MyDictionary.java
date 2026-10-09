package model.adt;

import model.exceptions.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private final Map<K, V> map;

    public MyDictionary() {
        map = new HashMap<>();
    }

    @Override
    public void put(K key, V value) throws ADTException {
        if (map.containsKey(key)) throw new ADTException("Key already defined: " + key);
        map.put(key, value);
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws ADTException {
        if (!map.containsKey(key)) throw new ADTException("Variable " + key + " is not defined");
        return map.get(key);
    }

    @Override
    public void update(K key, V value) throws ADTException {
        if (!map.containsKey(key)) throw new ADTException("Variable " + key + " is not defined");
        map.put(key, value);
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
