package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.expressions.Exp;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;
import model.exceptions.MyException;
import model.exceptions.ADTException;
import model.exceptions.ExprEvalException;

public class NewStmt implements IStmt {
    private final String varName;
    private final Exp expression;

    public NewStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();

        if (!symTable.isDefined(varName))
            throw new MyException("NewStmt: variable " + varName + " is not declared.");

        Value varValue;
        try {
            varValue = symTable.lookup(varName);
        } catch (Exception e) {
            throw new MyException("NewStmt: error reading variable " + varName + ": " + e.getMessage());
        }

        Type varType = varValue.getType();
        if (!(varType instanceof RefType))
            throw new MyException("NewStmt: variable " + varName + " is not of RefType.");

        IHeap heap = state.getHeap();
        if (heap == null)
            throw new MyException("NewStmt: Program state has no heap.");

        Value evaluated;
        try {
            // pass both symbol table and heap to eval
            evaluated = expression.eval(symTable, heap);
        } catch (ExprEvalException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("NewStmt: error evaluating expression: " + e.getMessage());
        }

        Type locationType = ((RefType) varType).getInner();
        if (!evaluated.getType().equals(locationType))
            throw new MyException("NewStmt: expression type " + evaluated.getType() +
                    " does not match variable location type " + locationType + ".");

        int newAddr;
        try {
            newAddr = heap.allocate(evaluated);
        } catch (Exception e) {
            throw new MyException("NewStmt: error allocating on heap: " + e.getMessage());
        }

        try {
            // use update instead of put to replace the existing variable binding
            symTable.update(varName, new RefValue(newAddr, locationType));
        } catch (ADTException e) {
            throw new MyException("NewStmt: error updating symbol table: " + e.getMessage());
        }

        return state;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type varType = typeEnv.lookup(varName);
        Type exprType = expression.typecheck(typeEnv);
        if (!(varType instanceof RefType))
            throw new MyException("NewStmt (typecheck): variable " + varName + " is not RefType.");
        RefType r = (RefType) varType;
        if (!r.getInner().equals(exprType))
            throw new MyException("NewStmt (typecheck): expression type " + exprType +
                    " does not match variable location type " + r.getInner() + ".");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression + ")";
    }
}
