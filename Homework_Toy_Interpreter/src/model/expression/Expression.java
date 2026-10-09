package model.expression;

import model.types.Type;
import model.value.Value;
import state.Heap;
import state.SymbolTable;

import java.util.Dictionary;
import java.util.Map;

public interface Expression {
    Value evaluate(SymbolTable symbolTable, Heap heap);
    Type typecheck(Map<String, Type> typeEnv) throws Exception;
}
