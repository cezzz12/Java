package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyDictionary;
import model.adt.MyIStack;
import model.adt.MyStack;
import model.adt.MyIList;
import model.adt.MyIFileTable;
import model.adt.IHeap;
import model.exceptions.ADTException;
import model.exceptions.MyException;
import model.values.Value;
import model.values.StringValue;
import model.types.Type;

import java.io.BufferedReader;
import java.util.Map;

public class ForkStmt implements IStmt {
    private final IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (state == null) throw new MyException("Fork: null program state");

        // clone the symbol table
        MyIDictionary<String, Value> newSym = new MyDictionary<>();
        Map<String, Value> content = state.getSymTable().getContent();
        try {
            for (Map.Entry<String, Value> e : content.entrySet()) {
                newSym.put(e.getKey(), e.getValue());
            }
        } catch (ADTException e) {
            throw new MyException("Fork: error cloning symbol table: " + e.getMessage());
        }


        MyIStack<IStmt> newStack = new MyStack<>();


        MyIFileTable<StringValue, BufferedReader> ft = state.getFileTable();
        IHeap heap = state.getHeap();
        MyIList<Value> out = state.getOut();

        // create new PrgState sharing fileTable and heap (no lock/semaphore/barrier support)
        return new PrgState(newStack, newSym, out, stmt, ft, heap);
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // clone the type environment so the fork's type declarations don't affect the parent
        MyIDictionary<String, Type> copy = new MyDictionary<>();
        try {
            for (Map.Entry<String, Type> e : typeEnv.getContent().entrySet()) {
                copy.put(e.getKey(), e.getValue());
            }
        } catch (ADTException e) {
            throw new MyException("Fork typecheck: error cloning type environment: " + e.getMessage());
        }
        // typecheck the forked statement using the cloned environment
        stmt.typecheck(copy);
        // return the original environment unchanged
        return typeEnv;
    }
}
