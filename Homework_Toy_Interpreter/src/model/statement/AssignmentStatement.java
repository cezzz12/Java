package model.statement;

import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.expression.Expression;
import model.types.Type;
import model.value.Value;
import state.ProgramState;
import state.SymbolTable;

import java.util.Dictionary;
import java.util.Map;

public record AssignmentStatement
        (String variableName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable symbolTable = state.symbolTable();
        if (!symbolTable.isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }

        Value value = expression.evaluate(symbolTable, state.heap());
        if (value.getType() != symbolTable.getVariableType(variableName)) {
            throw new InvalidTypeException("Type mismatch");
        }

        symbolTable.updateValue(variableName, value);
        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        Type typevar = typeEnv.get(variableName);
        Type typeexp = expression.typecheck(typeEnv);
        if(typevar.equals(typeexp)) {
            return typeEnv;
        }
        else throw new InvalidTypeException("Assignment statement type mismatch");
    }

    @Override
    public String toString() {
        return variableName + " = " + expression +";";
    }
}
