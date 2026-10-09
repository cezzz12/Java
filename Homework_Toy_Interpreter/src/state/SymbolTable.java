package state;

import model.types.Type;
import model.value.Value;

import java.util.Map;

public interface SymbolTable {
    boolean isDefined(String variableName);

    void declareVariable(Type type, String variableName);

    Type getVariableType(String variableName);

    void updateValue(String variableName, Value value);

    Value LookUp(String variableName);

    public Map<String, Value> getContents();

}
