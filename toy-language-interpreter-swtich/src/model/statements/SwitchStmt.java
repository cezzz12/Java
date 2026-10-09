package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.exceptions.MyException;
import model.expressions.Exp;
import model.types.Type;

public class SwitchStmt implements IStmt {
    private final Exp exp;
    private final Exp exp1;
    private final IStmt stmt1;
    private final Exp exp2;
    private final IStmt stmt2;
    private final IStmt defaultStmt;

    public SwitchStmt(Exp exp, Exp exp1, IStmt stmt1, Exp exp2, IStmt stmt2, IStmt defaultStmt) {
        this.exp = exp;
        this.exp1 = exp1;
        this.stmt1 = stmt1;
        this.exp2 = exp2;
        this.stmt2 = stmt2;
        this.defaultStmt = defaultStmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // on execution create: if(exp==exp1) then stmt1 else (if (exp==exp2) then stmt2 else defaultStmt)
        IStmt nested = new IfStmt(
                new model.expressions.RelExp("==", exp, exp1),
                stmt1,
                new IfStmt(new model.expressions.RelExp("==", exp, exp2), stmt2, defaultStmt)
        );
        state.getStk().push(nested);
        return state;
    }

    @Override
    public String toString() {
        return "(SWITCH(" + exp + ") (CASE(" + exp1 + ") : " + stmt1 + ") (CASE(" + exp2 + ") : " + stmt2 + ") (DEFAULT: " + defaultStmt + "))";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t = exp.typecheck(typeEnv);
        Type t1 = exp1.typecheck(typeEnv);
        Type t2 = exp2.typecheck(typeEnv);
        if (!t.equals(t1)) throw new MyException("Switch: exp and exp1 have different types");
        if (!t.equals(t2)) throw new MyException("Switch: exp and exp2 have different types");
        stmt1.typecheck(typeEnv);
        stmt2.typecheck(typeEnv);
        defaultStmt.typecheck(typeEnv);
        return typeEnv;
    }
}

