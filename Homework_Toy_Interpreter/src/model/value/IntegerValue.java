package model.value;

import model.types.Type;

public record IntegerValue(int value) implements Value {

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    public Value getValue() {
        return new  IntegerValue(value);
    }

    @Override
    public Value deepCopy() {
        return  new IntegerValue(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


}
