package model.types;

import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.StringValue;
import model.value.Value;

public interface Type {

    Value getDefaultValue();

    record Integer() implements Type {
        @Override
        public Value getDefaultValue() {
            return new IntegerValue(0);
        }
        @Override
        public java.lang.String toString() {
            return "int";
        }
    }

    record Boolean() implements Type {
        @Override
        public Value getDefaultValue() {
            return new BooleanValue(false);
        }
        @Override
        public java.lang.String toString() {
            return "boolean";
        }
    }

    record String() implements Type {
        @Override
        public Value getDefaultValue() {
            return new StringValue("");
        }
        @Override
        public java.lang.String toString() {
            return "string";
        }
    }

    Type INTEGER = new Integer();
    Type BOOLEAN = new Boolean();
    Type STRING = new String();
}