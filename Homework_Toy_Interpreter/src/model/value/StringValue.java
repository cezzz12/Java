package model.value;

import model.types.Type;

public record StringValue(String value) implements Value {
    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    public Value getValue() {
        return new StringValue(value);
    }

    @Override
    public Value deepCopy() {
        return new StringValue(value);
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

}
