package state;

import model.types.Type;
import model.value.Value;

import java.util.Map;

public interface Heap {
    int add(Value value);
    Value lookUp(int address);
    void remove(int address);
    void update(int address, Value value);
    Map<Integer, Value> getHeapMap();

    void setContent(Map<Integer, Value> heapMap);

}
