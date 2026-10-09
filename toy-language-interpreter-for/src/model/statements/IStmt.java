package model.statements;

import model.exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.types.Type;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
    default MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }
}
