package controller;

import model.values.RefValue;
import model.values.Value;

import java.util.*;
import java.util.stream.Collectors;

public final class GarbageCollector {
    private GarbageCollector() { /* utility class */ }

    /**
     * Unsafe collector: keeps only heap entries whose keys appear in symTableAddr.
     * Returns a mutable map.
     */
    public static Map<Integer, Value> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        if (symTableAddr == null || heap == null) return new HashMap<>();
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Extracts direct addresses from symbol table values (RefValue.address).
     */
    public static List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        if (symTableValues == null) return new ArrayList<>();
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    /**
     * Conservative/transitive collector: from roots in symTableValues, traverses RefValue links
     * inside heap to compute reachable addresses and returns a mutable heap map containing only them.
     */
    public static Map<Integer, Value> collectReachable(Collection<Value> symTableValues, Map<Integer, Value> heap) {
        if (heap == null || heap.isEmpty()) return new HashMap<>();

        Set<Integer> roots = new HashSet<>(getAddrFromSymTable(symTableValues));
        if (roots.isEmpty()) return new HashMap<>();

        Set<Integer> visited = new HashSet<>();
        Deque<Integer> worklist = new ArrayDeque<>();

        for (Integer r : roots) {
            if (r != null && r != 0 && heap.containsKey(r)) worklist.add(r);
        }

        while (!worklist.isEmpty()) {
            int addr = worklist.poll();
            if (!visited.add(addr)) continue;
            Value val = heap.get(addr);
            if (val instanceof RefValue) {
                int inner = ((RefValue) val).getAddr();
                if (inner != 0 && heap.containsKey(inner) && !visited.contains(inner)) {
                    worklist.add(inner);
                }
            }
        }

        return heap.entrySet().stream()
                .filter(e -> visited.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
