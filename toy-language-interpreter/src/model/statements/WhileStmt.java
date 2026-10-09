package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.values.BoolValue;
import model.values.Value;
import model.types.Type;
import model.types.BoolType;

/**
 * while (exp) stmt
 */
public class WhileStmt implements IStmt {
    private final model.expressions.Exp exp;
    private final IStmt stmt;

    public WhileStmt(model.expressions.Exp e, IStmt s) { exp = e; stmt = s; }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        Value cond = exp.eval(sym, state.getHeap());
        if (!(cond instanceof BoolValue)) throw new ExprEvalException("condition exp is not a boolean");
        boolean b = ((BoolValue) cond).getVal();
        MyIStack<IStmt> stk = state.getStk();
        if (!b) return null;
        // push stmt and the while again
        stk.push(this);
        stk.push(stmt);
        return null;
    }

    @Override
    public String toString() {
        return "while (" + exp + ") " + stmt;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type condType = exp.typecheck(typeEnv);
        if (!condType.equals(new BoolType())) throw new MyException("While: condition is not boolean");
        stmt.typecheck(typeEnv);
        return typeEnv;
    }
}
