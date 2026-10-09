package model.value;

import model.types.Type;

public record BooleanValue(boolean value) implements Value {

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public Value getValue() {
        return new BooleanValue(value);
    }

    @Override
    public Value deepCopy() {
        return new BooleanValue(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
