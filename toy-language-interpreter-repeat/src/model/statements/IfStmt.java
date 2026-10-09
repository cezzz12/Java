package model.statements;

import model.PrgState;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.values.BoolValue;
import model.values.Value;
import model.types.Type;
import model.types.BoolType;

public class IfStmt implements IStmt {
    private final model.expressions.Exp exp;
    private final IStmt thenS, elseS;
    public IfStmt(model.expressions.Exp e, IStmt t, IStmt el) { exp = e; thenS = t; elseS = el; }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        Value cond = exp.eval(sym, state.getHeap());
        if (!(cond instanceof BoolValue)) throw new ExprEvalException("Conditional expr is not a boolean");
        boolean b = ((BoolValue) cond).getVal();
        MyIStack<IStmt> stk = state.getStk();
        if (b) stk.push(thenS); else stk.push(elseS);
        return state;
    }

    @Override
    public String toString() {
        return "(IF(" + exp + ") THEN(" + thenS + ") ELSE(" + elseS + "))";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typecheck(typeEnv);
            elseS.typecheck(typeEnv);
            return typeEnv;
        } else
            throw new MyException("The condition of IF has not the type bool");
    }
}
