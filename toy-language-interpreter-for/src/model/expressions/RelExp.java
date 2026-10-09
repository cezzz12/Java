package model.expressions;

import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.Value;
import model.types.IntType;
import model.types.Type;
import model.types.BoolType;

public class RelExp implements Exp {
    private final String op;
    private final Exp e1, e2;

    public RelExp(String op, Exp e1, Exp e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException {
        Value v1 = e1.eval(tbl, hp);
        if (!(v1 instanceof IntValue)) throw new ExprEvalException("RelExp: first operand is not an integer");
        Value v2 = e2.eval(tbl, hp);
        if (!(v2 instanceof IntValue)) throw new ExprEvalException("RelExp: second operand is not an integer");
        int n1 = ((IntValue) v1).getVal();
        int n2 = ((IntValue) v2).getVal();
        switch (op) {
            case "<": return new BoolValue(n1 < n2);
            case "<=": return new BoolValue(n1 <= n2);
            case "==": return new BoolValue(n1 == n2);
            case "!=": return new BoolValue(n1 != n2);
            case ">": return new BoolValue(n1 > n2);
            case ">=": return new BoolValue(n1 >= n2);
            default: throw new ExprEvalException("RelExp: unknown operator " + op);
        }
    }

    @Override
    public String toString() {
        return "(" + e1.toString() + " " + op + " " + e2.toString() + ")";
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1 = e1.typecheck(typeEnv);
        Type t2 = e2.typecheck(typeEnv);
        if (t1.equals(new IntType())) {
            if (t2.equals(new IntType())) {
                return new BoolType();
            } else
                throw new MyException("RelExp: second operand is not an integer");
        } else
            throw new MyException("RelExp: first operand is not an integer");
    }
}
