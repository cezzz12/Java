package state;

import model.value.RefValue;
import model.value.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GarbageCollectorMap implements GarbageCollector{
    

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, Value> heapMap){
        // Get all reachable addresses
        List<Integer> reachableAddresses = getReachableAddresses(symTableAddresses, heapMap);
        
        return heapMap.entrySet().stream()
                .filter(e -> reachableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Integer> getReachableAddresses(List<Integer> addresses, Map<Integer, Value> heapMap) {
        // Get addresses from heap values at current addresses
        List<Integer> newAddresses = addresses.stream()
                .filter(heapMap::containsKey)
                .map(heapMap::get)
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .filter(addr -> !addresses.contains(addr))
                .distinct()
                .collect(Collectors.toList());
        
        if (newAddresses.isEmpty()) {
            return addresses;
        }
        
        List<Integer> combined = Stream.concat(addresses.stream(), newAddresses.stream())
                .distinct()
                .collect(Collectors.toList());
        
        return getReachableAddresses(combined, heapMap);
    }
    
    public Map<Integer, Value> unsafeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, Value> heapMap){
        return heapMap.entrySet().stream()
                .filter(e-> symTableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    @Override
    public List<Integer> getAddressesFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v-> v instanceof RefValue)
                .map(v-> {RefValue refValue = (RefValue) v; return refValue.getAddress();})
                .collect(Collectors.toList());
    }
}
