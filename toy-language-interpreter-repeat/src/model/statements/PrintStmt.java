package model.statements;

import model.PrgState;
import model.adt.MyIList;
import model.adt.MyIDictionary;
import model.exceptions.MyException;
import model.values.Value;
import model.types.Type;

public class PrintStmt implements IStmt {
    private final model.expressions.Exp exp;
    public PrintStmt(model.expressions.Exp e) { exp = e; }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        Value val = exp.eval(sym,state.getHeap());
        MyIList<Value> out = state.getOut();
        out.add(val);
        return null;
    }

    @Override
    public String toString() { return "Print(" + exp.toString() + ")"; }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }
}
