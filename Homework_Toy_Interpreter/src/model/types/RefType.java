package model.types;

import model.value.RefValue;
import model.value.Value;

public record RefType(Type inner) implements Type {
    public boolean equals(Object other) {
        if (other instanceof RefType) {
            return inner.equals(((RefType) other).inner());
        }
        return false;
    }

    @Override
    public java.lang.String toString() {
        return "RefType{" +
                "inner=" + inner +
                '}';
    }

    @Override
    public Value getDefaultValue() {
        return new RefValue(0, inner);
    }

}
