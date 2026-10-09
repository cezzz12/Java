package model.expression;

import model.types.Type;
import model.value.Value;
import state.Heap;
import state.SymbolTable;

import java.util.Dictionary;
import java.util.Map;

public record ValueExpression(Value value) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        return value;
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws Exception {
        return value.getType();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
