// File: src/model/expressions/ReadHeapExpr.java
package model.expressions;

import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.values.RefValue;
import model.values.Value;
import model.types.RefType;
import model.types.Type;

public class ReadHeapExpr implements Exp {
    private final Exp inner;

    public ReadHeapExpr(Exp inner) {
        this.inner = inner;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException {
        if (hp == null) throw new ExprEvalException("rH: heap is null");

        Value v = inner.eval(tbl, hp);
        if (!(v instanceof RefValue))
            throw new ExprEvalException("rH: expression is not a RefValue");

        RefValue ref = (RefValue) v;
        int addr = ref.getAddr();
        if (!hp.containsKey(addr))
            throw new ExprEvalException("rH: address " + addr + " not present in heap");

        return hp.get(addr);
    }

    @Override
    public String toString() {
        return "rH(" + inner.toString() + ")";
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = inner.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else
            throw new MyException("ReadHeapExpr: the rH argument is not a Ref Type");
    }
}
