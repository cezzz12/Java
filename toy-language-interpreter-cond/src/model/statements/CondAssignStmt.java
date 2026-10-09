package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.exceptions.MyException;
import model.types.BoolType;
import model.types.Type;

public class CondAssignStmt implements IStmt {
    private final String varName;
    private final model.expressions.Exp condExp;
    private final model.expressions.Exp thenExp;
    private final model.expressions.Exp elseExp;

    public CondAssignStmt(String varName, model.expressions.Exp condExp, model.expressions.Exp thenExp, model.expressions.Exp elseExp) {
        this.varName = varName;
        this.condExp = condExp;
        this.thenExp = thenExp;
        this.elseExp = elseExp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // Execution: replace cond-assign with an equivalent if statement on the stack
        model.adt.MyIStack<IStmt> stk = state.getStk();
        IStmt thenStmt = new AssignStmt(varName, thenExp);
        IStmt elseStmt = new AssignStmt(varName, elseExp);
        IStmt ifStmt = new IfStmt(condExp, thenStmt, elseStmt);
        stk.push(ifStmt);
        return state;
    }

    @Override
    public String toString() {
        return varName + " = " + condExp.toString() + " ? " + thenExp.toString() + " : " + elseExp.toString();
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type condType = condExp.typecheck(typeEnv);
        if (!condType.equals(new BoolType()))
            throw new MyException("CondAssign: condition expression is not a boolean");
        Type varType = typeEnv.lookup(varName);
        Type tType = thenExp.typecheck(typeEnv);
        Type eType = elseExp.typecheck(typeEnv);
        if (!varType.equals(tType) || !varType.equals(eType))
            throw new MyException("CondAssign: variable and both branches must have the same type");
        return typeEnv;
    }
}
