package model.statements;

import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyIDictionary;
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


public class SyncForkStmt implements IStmt {
    private final IStmt stmt;

    public SyncForkStmt(IStmt stmt) { this.stmt = stmt; }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (state == null) throw new MyException("SyncFork: null program state");

        // clone the symbol table
        MyIDictionary<String, Value> newSym = new MyDictionary<>();
        Map<String, Value> content = state.getSymTable().getContent();
        try {
            for (Map.Entry<String, Value> e : content.entrySet()) {
                newSym.put(e.getKey(), e.getValue());
            }
        } catch (ADTException e) {
            throw new MyException("SyncFork: error cloning symbol table: " + e.getMessage());
        }

        MyIStack<IStmt> newStack = new MyStack<>();
        MyIList<Value> out = state.getOut();
        MyIFileTable<StringValue, BufferedReader> ft = state.getFileTable();
        IHeap heap = state.getHeap();

        PrgState child = new PrgState(newStack, newSym, out, stmt, ft, heap);
        // execute child to completion synchronously
        while (child.isNotCompleted()) {
            child.oneStep();
        }
        return null; // no new prg to schedule
    }

    @Override
    public String toString() { return "syncfork(" + stmt.toString() + ")"; }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // clone env and typecheck stmt
        MyIDictionary<String, Type> copy = new MyDictionary<>();
        try {
            for (Map.Entry<String, Type> e : typeEnv.getContent().entrySet()) copy.put(e.getKey(), e.getValue());
        } catch (ADTException e) { throw new MyException("SyncFork typecheck: " + e.getMessage()); }
        stmt.typecheck(copy);
        return typeEnv;
    }
}

