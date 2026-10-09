package model.values;

import model.types.IntType;
import model.types.Type;

public class IntValue implements Value {
    private final int val;
    public IntValue(int v) { val = v; }
    public int getVal() { return val; }
    @Override
    public Type getType() { return new IntType(); }
    @Override
    public String toString() { return Integer.toString(val); }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof IntValue)) return false;
        return ((IntValue) another).getVal() == val;
    }
}
