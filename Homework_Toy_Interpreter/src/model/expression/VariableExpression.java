package model.expression;

import model.exception.VariableNotDefinedException;
import model.types.Type;
import model.value.Value;
import state.Heap;
import state.SymbolTable;

import java.util.Dictionary;
import java.util.Map;

public record VariableExpression(
        String variableName) implements Expression  {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        if (!symbolTable.isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }
        return symbolTable.LookUp(variableName);
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws Exception {
        return typeEnv.get(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }
}
