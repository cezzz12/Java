package model.expressions;

import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.values.BoolValue;
import model.values.Value;
import model.types.Type;
import model.types.BoolType;

public class LogicExp implements Exp {
    // op: "and" or "or"
    private final String op;
    private final Exp e1, e2;
    public LogicExp(String op, Exp e1, Exp e2) { this.op = op; this.e1 = e1; this.e2 = e2; }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException {
        Value v1 = e1.eval(tbl, hp);
        if (!(v1 instanceof BoolValue)) throw new ExprEvalException("Operand1 is not a boolean");
        Value v2 = e2.eval(tbl, hp);
        if (!(v2 instanceof BoolValue)) throw new ExprEvalException("Operand2 is not a boolean");
        boolean b1 = ((BoolValue) v1).getVal();
        boolean b2 = ((BoolValue) v2).getVal();
        switch (op) {
            case "and": return new BoolValue(b1 && b2);
            case "or": return new BoolValue(b1 || b2);
            default: throw new ExprEvalException("Unknown logical operator: " + op);
        }
    }

    @Override
    public String toString() { return "(" + e1.toString() + " " + op + " " + e2.toString() + ")"; }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1 = e1.typecheck(typeEnv);
        Type t2 = e2.typecheck(typeEnv);
        if (t1.equals(new BoolType())) {
            if (t2.equals(new BoolType())) {
                return new BoolType();
            } else
                throw new MyException("LogicExp: second operand is not a boolean");
        } else
            throw new MyException("LogicExp: first operand is not a boolean");
    }
}
