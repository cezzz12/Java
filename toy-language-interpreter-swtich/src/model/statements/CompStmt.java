package model.statements;

import model.PrgState;
import model.exceptions.MyException;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.types.Type;

public class CompStmt implements IStmt {
    private final IStmt first, snd;
    public CompStmt(IStmt f, IStmt s) { first = f; snd = s; }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();
        stk.push(snd);
        stk.push(first);
        return null;
    }

    @Override
    public String toString() { return "(" + first.toString() + "; " + snd.toString() + ")"; }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        MyIDictionary<String, Type> env1 = first.typecheck(typeEnv);
        return snd.typecheck(env1);
    }
}
