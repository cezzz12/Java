package state;

import model.value.Value;

import java.util.List;

public interface Out {
    void append(Value value);
    public List<Value> getContents();
}
