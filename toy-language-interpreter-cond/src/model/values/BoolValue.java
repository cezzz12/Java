package model.values;

import model.types.BoolType;
import model.types.Type;

public class BoolValue implements Value {
    private final boolean val;
    public BoolValue(boolean v) { val = v; }
    public boolean getVal() { return val; }
    @Override
    public Type getType() { return new BoolType(); }
    @Override
    public String toString() { return Boolean.toString(val); }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof BoolValue)) return false;
        return ((BoolValue) another).getVal() == val;
    }
}
