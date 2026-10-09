package model.types;
import model.values.Value;

public interface Type {
    boolean equals(Object another);
    String toString();
    Value defaultValue();
}
