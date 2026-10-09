package state;

import model.types.Type;
import model.value.Value;
import state.exceptions.VariableNotInTableException;

import java.util.HashMap;
import java.util.Map;

public class MapSymbolTable implements SymbolTable {
    private final Map<String, Value> symbolTable = new HashMap<>();

    @Override
    public boolean isDefined(String variableName) {
        return symbolTable.containsKey(variableName);
    }

    @Override
    public void declareVariable(Type type, String variableName) {
        symbolTable.put(variableName, type.getDefaultValue());
    }

    @Override
    public Type getVariableType(String variableName) {
        if  (!symbolTable.containsKey(variableName)) {
            throw new VariableNotInTableException("String variable is not in the symbol table");
        }
        return symbolTable.get(variableName).getType();
    }

    @Override
    public void updateValue(String variableName, Value value) {
        symbolTable.put(variableName, value);
    }

    @Override
    public Value LookUp(String variableName) {
        return symbolTable.get(variableName);
    }

    @Override
    public String toString() {
        return "Symbol Table: " + symbolTable.toString();
    }

    public Map<String, Value> getContents() {
        return symbolTable;
    }
}
