package model.statements;

import model.PrgState;
import model.exceptions.MyException;
import model.adt.MyIDictionary;
import model.types.Type;

public class NopStmt implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        // do nothing
        return state;
    }

    @Override
    public String toString() { return "nop"; }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }
}
