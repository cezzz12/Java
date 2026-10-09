package state;

import model.value.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GarbageCollector {
    Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, Value> heapMap);
    Map<Integer, Value> unsafeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, Value> heapMap);
    List<Integer> getAddressesFromSymTable(Collection<Value> symTableValues);
}
