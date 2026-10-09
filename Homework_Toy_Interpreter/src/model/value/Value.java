package model.value;

import model.types.Type;

public interface Value {
    Type getType();
    Value getValue();
    Value deepCopy();
}
