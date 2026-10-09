package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.exceptions.StmtExecException;
import model.types.Type;
import model.values.Value;

public class AssignStmt implements IStmt {
    private final String id;
    private final model.expressions.Exp exp;
    public AssignStmt(String id, model.expressions.Exp e) { this.id = id; this.exp = e; }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        if (!sym.isDefined(id)) throw new StmtExecException("Variable " + id + " is not declared");
        Value val = exp.eval(sym, state.getHeap());
        Value oldVal = sym.lookup(id);
        if (!val.getType().equals(oldVal.getType()))
            throw new StmtExecException("Type of expression and type of variable do not match for " + id);
        sym.update(id, val);
        return null;
    }

    @Override
    public String toString() { return id + " = " + exp.toString(); }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type varType = typeEnv.lookup(id);
        Type expType = exp.typecheck(typeEnv);
        if (varType.equals(expType))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types");
    }
}
