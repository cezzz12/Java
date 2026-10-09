package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.exceptions.MyException;
import model.types.BoolType;
import model.types.Type;

/**
 * repeat stmt until exp
 * semantics: execute stmt once and then while(!exp) stmt
 */
public class RepeatStmt implements IStmt {
    private final IStmt stmt;
    private final model.expressions.Exp exp;

    public RepeatStmt(IStmt stmt, model.expressions.Exp exp) {
        this.stmt = stmt;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // push the composed statement: stmt; (while(!exp) stmt)
        IStmt composed = new CompStmt(stmt, new WhileStmt(new model.expressions.NotExp(exp), stmt));
        state.getStk().push(composed);
        return null;
    }

    @Override
    public String toString() {
        return "repeat(" + stmt.toString() + ") until(" + exp.toString() + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t = exp.typecheck(typeEnv);
        if (!t.equals(new BoolType())) throw new MyException("Repeat: condition is not boolean");
        stmt.typecheck(typeEnv);
        return typeEnv;
    }
}

