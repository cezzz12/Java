package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIFileTable;
import model.exceptions.ExprEvalException;
import model.exceptions.MyException;
import model.exceptions.StmtExecException;
import model.expressions.Exp;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class OpenRFileStmt implements IStmt {
    private final Exp exp;

    public OpenRFileStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> sym = state.getSymTable();
        // pass the heap as the second argument
        Value v = exp.eval(sym, state.getHeap());
        if (!(v instanceof StringValue)) throw new ExprEvalException("openRFile: expression is not a string");
        StringValue sv = (StringValue) v;

        Object ftObj = getFileTableObject(state);
        if (ftObj == null) throw new StmtExecException("openRFile: Program state has no file table");
        if (!(ftObj instanceof MyIFileTable)) throw new StmtExecException("openRFile: file table has unexpected type");

        @SuppressWarnings("unchecked")
        MyIFileTable<StringValue, BufferedReader> ft = (MyIFileTable<StringValue, BufferedReader>) ftObj;

        if (ft.containsKey(sv)) throw new StmtExecException("openRFile: file already opened: " + sv.getVal());
        try {
            BufferedReader br = new BufferedReader(new FileReader(sv.getVal()));
            ft.put(sv, br);
        } catch (IOException e) {
            throw new StmtExecException("openRFile: IO error: " + e.getMessage());
        }
        return state;
    }

    private Object getFileTableObject(PrgState state) {
        if (state == null) return null;
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
        Type t = exp.typecheck(typeEnv);
        if (!t.equals(new StringType())) throw new MyException("openRFile: argument is not a string");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }
}
