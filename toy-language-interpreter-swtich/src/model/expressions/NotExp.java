// ...existing code...
package model.expressions;

import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

/**
 * Logical negation: !exp
 */
public class NotExp implements Exp {
    private final Exp e;

    public NotExp(Exp e) { this.e = e; }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException {
        Value v = e.eval(tbl, hp);
        if (!(v instanceof BoolValue)) throw new ExprEvalException("Not: operand is not a boolean");
        boolean b = ((BoolValue) v).getVal();
        return new BoolValue(!b);
    }

    @Override
    public String toString() { return "(!" + e.toString() + ")"; }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t = e.typecheck(typeEnv);
        if (!t.equals(new BoolType())) throw new MyException("Not: operand is not boolean");
        return new BoolType();
    }
}

