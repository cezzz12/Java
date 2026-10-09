package model.value;

import model.types.RefType;
import model.types.Type;

public record RefValue(int address, Type locationType) implements Value {


    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    public Type getLocationType(){
        return locationType;
    }

    @Override
    public Value getValue() {
        return new RefValue(address,locationType);
    }

    @Override
    public Value deepCopy() {
        return new RefValue(address,locationType);
    }

    public int getAddress(){
        return address;
    }

    @Override
    public String toString() {
        return "RefValue{" +
                "address=" + address +
                ", locationType=" + locationType +
                '}';
    }
}
