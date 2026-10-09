package repository;

import model.statement.Statement;
import model.value.Value;
import repository.exception.FileRepositoryException;
import repository.exception.ProgramStateNotFound;
import state.ProgramState;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListRepository implements Repository {
    private final List<ProgramState> programStates = new ArrayList<>();
    private final PrintWriter logFile;
    public ListRepository(String logFilePath){
        //IO.println("Loading program states...");
        try {
            this.logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath)));
        }
        catch (Exception e) {
            throw new FileRepositoryException(e.getMessage());
        }
    }
//    public ProgramState getCurrentProgramState(){
//        return programStates.getFirst();
//    }
    public void addProgramState(ProgramState programState){
        programStates.add(programState);
    }
    public void removeProgramState(ProgramState programState){
        if (!programStates.contains(programState)){
            throw new ProgramStateNotFound("Program state not found, try removing an existing program state");
        }
        programStates.remove(programState);
    }
    public void logProgramStateExecution(ProgramState currentProgramState){
        this.logFile.println("Id: ");
        this.logFile.println(currentProgramState.id());
        this.logFile.println("ExeStack:");
        List<Statement> stackItems = new ArrayList<>(currentProgramState.executionStack().getContents());
        for (Statement item : stackItems) {
            this.logFile.println(item.toString());
        }

        this.logFile.println("SymTable:");
        for (Map.Entry<String, Value> entry : currentProgramState.symbolTable().getContents().entrySet()) {
            this.logFile.println(entry.getKey() + " --> " + entry.getValue().toString());
        }

        this.logFile.println("Out:");
        for (Value item : currentProgramState.out().getContents()) {
            this.logFile.println(item.toString());
        }

        this.logFile.println("FileTable:");

        for (Value filename : currentProgramState.fileTable().getContents().keySet()) {
            this.logFile.println(filename.toString());
        }
        this.logFile.println("HeapTable:");
        for (var val:currentProgramState.heap().getHeapMap().keySet()){
            this.logFile.print(val.toString());
            this.logFile.println(" --> " + currentProgramState.heap().lookUp(val).toString());
        }
        this.logFile.println("----------------------------------------\n");
        this.logFile.flush();
    }

    @Override
    public List<ProgramState> getProgramStates() {
        return programStates;
    }

    @Override
    public void setProgramList(List<ProgramState> programStates) {
        this.programStates.clear();
        this.programStates.addAll(programStates);
    }
}
