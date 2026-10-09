package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIFileTable;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.exceptions.StmtExecException;
import model.expressions.Exp;
import model.types.IntType;
import model.types.StringType;
import model.types.Type;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReadFileStmt implements IStmt {
    private final Exp exp;
    private final String varName;

    public ReadFileStmt(Exp exp, String varName) {
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        if (!sym.isDefined(varName)) throw new StmtExecException("readFile: variable " + varName + " not defined");
        if (!(sym.lookup(varName).getType() instanceof IntType))
            throw new StmtExecException("readFile: variable " + varName + " is not of type int");

        // pass heap as second argument
        Value v = exp.eval(sym, state.getHeap());
        if (!(v instanceof StringValue)) throw new ExprEvalException("readFile: expression is not a string");
        StringValue sv = (StringValue) v;

        Object ftObj = getFileTableObject(state);
        if (ftObj == null) throw new StmtExecException("readFile: Program state has no file table");

        if (!(ftObj instanceof MyIFileTable))
            throw new StmtExecException("readFile: file table has unexpected type");

        @SuppressWarnings("unchecked")
        MyIFileTable<StringValue, BufferedReader> ft = (MyIFileTable<StringValue, BufferedReader>) ftObj;

        if (!ft.containsKey(sv)) throw new StmtExecException("readFile: file not opened: " + sv.getVal());
        BufferedReader br = ft.get(sv);
        try {
            String line = br.readLine();
            IntValue toStore;
            if (line == null) toStore = new IntValue(0);
            else toStore = new IntValue(Integer.parseInt(line.trim()));
            sym.update(varName, toStore);
        } catch (IOException e) {
            throw new StmtExecException("readFile: IO error: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new StmtExecException("readFile: parse int error: " + e.getMessage());
        }
        return state;
    }

    private Object getFileTableObject(PrgState state) {
        if (state == null) return null;
        // try common getter names
        String[] methodNames = {"getFileTable", "getFileTbl", "getFileTableRef", "getFiletable"};
        for (String name : methodNames) {
            try {
                Method m = state.getClass().getMethod(name);
                Object res = m.invoke(state);
                if (res != null) return res;
            } catch (NoSuchMethodException ignored) {
            } catch (Exception ignored) {
            }
        }
        // try a field named fileTable (private allowed)
        try {
            Field f = state.getClass().getDeclaredField("fileTable");
            f.setAccessible(true);
            Object res = f.get(state);
            if (res != null) return res;
        } catch (NoSuchFieldException ignored) {
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type texp = exp.typecheck(typeEnv);
        if (!texp.equals(new StringType()))
            throw new MyException("readFile: expression is not a string");
        Type tv = typeEnv.lookup(varName);
        if (!tv.equals(new IntType()))
            throw new MyException("readFile: variable " + varName + " is not of type int");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "readFile(" + exp.toString() + ", " + varName + ")";
    }
}
