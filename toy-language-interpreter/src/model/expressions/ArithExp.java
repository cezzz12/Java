package model.expressions;

import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.values.IntValue;
import model.values.Value;
import model.types.IntType;
import model.types.Type;

public class ArithExp implements Exp {
    // op: '+', '-', '*', '/'
    private final char op;
    private final Exp e1, e2;
    public ArithExp(char op, Exp e1, Exp e2) { this.op = op; this.e1 = e1; this.e2 = e2; }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException {
        Value v1 = e1.eval(tbl, hp);
        if (!(v1 instanceof IntValue)) throw new ExprEvalException("Operand1 is not an integer");
        Value v2 = e2.eval(tbl, hp);
        if (!(v2 instanceof IntValue)) throw new ExprEvalException("Operand2 is not an integer");
        int n1 = ((IntValue) v1).getVal();
        int n2 = ((IntValue) v2).getVal();
        switch (op) {
            case '+': return new IntValue(n1 + n2);
            case '-': return new IntValue(n1 - n2);
            case '*': return new IntValue(n1 * n2);
            case '/':
                if (n2 == 0) throw new ExprEvalException("Division by zero");
                return new IntValue(n1 / n2);
            default: throw new ExprEvalException("Unknown arithmetic operator: " + op);
        }
    }

    @Override
    public String toString() { return "(" + e1.toString() + " " + op + " " + e2.toString() + ")"; }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1 = e1.typecheck(typeEnv);
        Type typ2 = e2.typecheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else
                throw new MyException("ArithExp: second operand is not an integer");
        } else
            throw new MyException("ArithExp: first operand is not an integer");
    }
}
