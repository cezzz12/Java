package model.values;

import model.types.StringType;
import model.types.Type;

public class StringValue implements Value {
    private final String val;
    public StringValue(String v) { val = v; }
    public String getVal() { return val; }
    @Override
    public Type getType() { return new StringType(); }
    @Override
    public String toString() { return val; }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof StringValue)) return false;
        return ((StringValue) another).getVal().equals(val);
    }
}
