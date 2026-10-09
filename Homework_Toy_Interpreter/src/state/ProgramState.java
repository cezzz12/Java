package state;

import model.statement.Statement;
import state.exceptions.EmptyStackException;

public record ProgramState(
        int id,
        ExecutionStack executionStack,
        SymbolTable symbolTable,
        Out out,
        FileTable fileTable,
        Heap heap) {
    
    private static int nextId = 1;
    
    public static synchronized int getNextId() {
        return nextId++;
    }
    
    // Constructor without ID (generates new ID)
    public ProgramState(
            ExecutionStack executionStack,
            SymbolTable symbolTable,
            Out out,
            FileTable fileTable,
            Heap heap) {
        this(getNextId(), executionStack, symbolTable, out, fileTable, heap);
    }
    
    Boolean isNotCompleted(){
        return !executionStack.isEmpty();
    }
    
    public ProgramState oneStep(){
        if (executionStack.isEmpty()){
            throw new EmptyStackException("Cannot execute empty program state stack");
        }
        Statement currentStatement = executionStack.pop();
        return currentStatement.execute(this);
    }
    
    @Override
    public String toString() {
        return "ProgramState ID: " + id + "\n" +
               "ExeStack:\n" + executionStack.toString() +
               "SymTable:\n" + symbolTable.toString() +
               "Out:\n" + out.toString() +
               "FileTable:\n" + fileTable.toString() +
               "HeapTable:\n" + heap.toString();
    }
}
