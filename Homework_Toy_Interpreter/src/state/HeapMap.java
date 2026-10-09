package state;

import model.types.Type;
import model.value.Value;
import state.exceptions.AddressAlreadyUsed;
import state.exceptions.AddressNotInHeap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HeapMap implements Heap{
    private final Map<Integer, Value> heapMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentAddress = new AtomicInteger(1);

    @Override
    public int add(Value value) {
        // incrementAndGet is atomic (safe)
        int addr = currentAddress.getAndIncrement();
        heapMap.put(addr, value);
        return addr;
    }

    @Override
    public Value lookUp(int address) {
        if (heapMap.containsKey(address)) {
            return heapMap.get(address).getValue();
        }
        throw new AddressNotInHeap("Address not in heap");

    }

    @Override
    public void remove(int address) {
        if (heapMap.containsKey(address)) {
            heapMap.remove(address);
            return;
        }
        else {
            throw new AddressNotInHeap("Address not in heap");
        }
    }

    @Override
    public void update(int address, Value value) {
        if (heapMap.containsKey(address)) {
            heapMap.put(address, value);
        }
        else{
            throw new AddressNotInHeap("Address not in heap");
        }
    }
    @Override
    public Map<Integer, Value> getHeapMap() {
        return heapMap;
    }

    @Override
    public void setContent(Map<Integer, Value> heapMap) {
        this.heapMap.clear();
        this.heapMap.putAll(heapMap);
    }
}
