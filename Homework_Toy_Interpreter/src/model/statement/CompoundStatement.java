package model.statement;

import model.types.Type;
import state.ExecutionStack;
import state.ProgramState;

import java.util.Dictionary;
import java.util.Map;

public record CompoundStatement
        (Statement first, Statement second) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        ExecutionStack executionStack = state.executionStack();
        executionStack.push(second);
        executionStack.push(first);

        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        return second.typeCheck(first.typeCheck(typeEnv));
    }

    @Override
    public String toString() {
        return first.toString() + "\n" + second.toString();
    }
}
