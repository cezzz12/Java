package model.expressions;

import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.exceptions.MyException;
import model.values.Value;
import model.types.Type;

public class ValueExp implements Exp {
    private final Value val;
    public ValueExp(Value v) { this.val = v; }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException {
        return val;
    }

    @Override
    public String toString() { return val.toString(); }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return val.getType();
    }
}
