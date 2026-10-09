package model.statement;

import model.types.Type;
import state.ProgramState;

import java.util.Dictionary;
import java.util.Map;

public interface Statement {
    ProgramState execute(ProgramState state);
    Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception;
}
