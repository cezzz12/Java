package model;

import model.adt.MyIList;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.adt.MyIFileTable;
import model.adt.MyFileTable;
import model.adt.IHeap;
import model.adt.Heap;
import model.statements.IStmt;
import model.values.Value;
import model.values.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private final MyIStack<IStmt> exeStack;
    private final MyIDictionary<String, Value> symTable;
    private final MyIList<Value> out;
    private final IStmt originalProgram;
    private final MyIFileTable<StringValue, BufferedReader> fileTable;
    private final IHeap heap;

    // id management for concurrency: unique program ids
    private final int id;
    private static int lastId = 0;
    private static synchronized int getNewId() { return ++lastId; }

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl, MyIList<Value> ot, IStmt prg) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.originalProgram = prg;
        // assign unique id
        this.id = getNewId();
        // push original program on stack
        if (this.exeStack != null && this.originalProgram != null) this.exeStack.push(this.originalProgram);
        this.fileTable = new MyFileTable<>();
        this.heap = new Heap();
    }

    // Alternate constructor: supply existing file table and heap
    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl, MyIList<Value> ot, IStmt prg,
                    MyIFileTable<StringValue, BufferedReader> fileTable, IHeap heap) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.originalProgram = prg;
        this.id = getNewId();
        if (this.exeStack != null && this.originalProgram != null) this.exeStack.push(this.originalProgram);
        this.fileTable = fileTable != null ? fileTable : new MyFileTable<>();
        this.heap = heap != null ? heap : new Heap();
    }

    public MyIStack<IStmt> getStk() { return exeStack; }
    public MyIDictionary<String, Value> getSymTable() { return symTable; }
    public MyIList<Value> getOut() { return out; }
    public IStmt getOriginalProgram() { return originalProgram; }
    public MyIFileTable<StringValue, BufferedReader> getFileTable() { return fileTable; }
    public IHeap getHeap() { return heap; }
    public int getId() { return id; }

    // returns true if there are still statements to execute
    public Boolean isNotCompleted() { return !exeStack.isEmpty(); }

    // move oneStep into PrgState: pops one stmt and executes it
    public PrgState oneStep() throws model.exceptions.MyException {
        if (exeStack == null || exeStack.isEmpty())
            throw new model.exceptions.MyException("prgstate stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "PrgState ID=" + id + " {\n" +
                " exeStack=" + (exeStack != null ? exeStack.toString() : "null") +
                ",\n symTable=" + (symTable != null ? symTable.toString() : "null") +
                ",\n out=" + (out != null ? out.toString() : "null") +
                ",\n fileTable=" + (fileTable != null ? fileTable.toString() : "null") +
                ",\n heap=" + (heap != null ? heap.toString() : "null") +
                "\n}";
    }
}
