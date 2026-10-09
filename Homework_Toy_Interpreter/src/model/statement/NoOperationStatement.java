package model.statement;

import model.types.Type;
import state.ProgramState;

import java.util.Map;

public class NoOperationStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "No Operation";
    }
}
