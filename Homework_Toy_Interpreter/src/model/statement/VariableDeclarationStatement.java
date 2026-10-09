package model.statement;

import model.types.Type;
import model.exception.VariableAlreadyDefinedException;
import state.ProgramState;
import state.SymbolTable;

import java.util.Dictionary;
import java.util.Map;

public record VariableDeclarationStatement(Type type, String variableName) implements Statement {


    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable symbolTable = state.symbolTable();
        if (symbolTable.isDefined(variableName)) {
            throw new VariableAlreadyDefinedException();
        }

        symbolTable.declareVariable(type, variableName);
        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        typeEnv.put(variableName, type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type + " " + variableName + ";";
    }
}
