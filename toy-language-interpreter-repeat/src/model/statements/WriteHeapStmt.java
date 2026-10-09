package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.expressions.Exp;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.exceptions.StmtExecException;
import model.values.RefValue;
import model.values.Value;
import model.types.RefType;
import model.types.Type;

public class WriteHeapStmt implements IStmt {
    private final String varName;
    private final Exp expression;

    public WriteHeapStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        if (!sym.isDefined(varName))
            throw new StmtExecException("wH: variable " + varName + " is not defined");

        Value varVal = sym.lookup(varName);
        if (!(varVal instanceof RefValue))
            throw new StmtExecException("wH: variable " + varName + " is not a RefValue");

        RefValue ref = (RefValue) varVal;
        int addr = ref.getAddr();

        IHeap hp = state.getHeap();
        if (hp == null)
            throw new StmtExecException("wH: program state has no heap");

        if (!hp.containsKey(addr))
            throw new StmtExecException("wH: address " + addr + " not present in heap");

        Value evaluated = expression.eval(sym, hp);
        if (!evaluated.getType().equals(ref.getLocationType()))
            throw new StmtExecException("wH: expression type " + evaluated.getType() +
                    " does not match location type " + ref.getLocationType());

        hp.update(addr, evaluated);

        return null;
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type varType = typeEnv.lookup(varName);
        if (!(varType instanceof RefType))
            throw new MyException("wH: variable " + varName + " is not a RefType");
        RefType r = (RefType) varType;
        Type exprType = expression.typecheck(typeEnv);
        if (!exprType.equals(r.getInner()))
            throw new MyException("wH: expression type " + exprType + " does not match location type " + r.getInner());
        return typeEnv;
    }
}
