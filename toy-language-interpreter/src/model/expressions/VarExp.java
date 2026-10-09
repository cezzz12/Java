package model.expressions;

import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.exceptions.MyException;
import model.values.Value;
import model.types.Type;

public class VarExp implements Exp {
    private final String id;
    public VarExp(String i) { id = i; }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException {
        if (tbl == null) throw new MyException("VarExp: symbol table is null");
        return tbl.lookup(id);
    }

    @Override
    public String toString() { return id; }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }
}
