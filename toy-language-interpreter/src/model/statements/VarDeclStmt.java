package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.exceptions.MyException;
import model.exceptions.StmtExecException;
import model.types.Type;
import model.values.Value;

public class VarDeclStmt implements IStmt {
    private final String name;
    private final Type type;
    public VarDeclStmt(String n, Type t) { name = n; type = t; }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        if (sym.isDefined(name)) throw new StmtExecException("Variable is already declared: " + name);
        // use the type's default value (per spec)
        sym.put(name, type.defaultValue());
        return null;
    }

    @Override
    public String toString() { return type.toString() + " " + name; }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
