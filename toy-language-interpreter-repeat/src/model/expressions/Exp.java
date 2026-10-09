// java
package model.expressions;

import model.exceptions.MyException;
import model.adt.MyIDictionary;
import model.adt.IHeap;
import model.values.Value;
import model.types.Type;

public interface Exp {
    Value eval(MyIDictionary<String, Value> tbl, IHeap hp) throws MyException;
    Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
