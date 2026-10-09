package model.values;

import model.types.Type;
import model.types.RefType;

public class RefValue implements Value {

    private final int address;
    private final Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddr() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RefValue) {
            RefValue refOther = (RefValue) other;
            return address == refOther.address &&
                    locationType.equals(refOther.locationType);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }
}
