package model.adt;

import model.values.Value;
import model.exceptions.MyException;
import java.util.Map;

public interface IHeap {
    int allocate(Value value);                    // returns newly allocated address (starts from 1)
    Value get(int address) throws MyException;    // read value at address
    void put(int address, Value value);           // insert or overwrite at address
    boolean containsKey(int address);
    void update(int address, Value value) throws MyException; // update existing address
    void remove(int address) throws MyException;  // remove mapping
    Map<Integer, Value> getContent();
    void setContent(Map<Integer, Value> content);
}
