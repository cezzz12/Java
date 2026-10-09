package repository;

import model.PrgState;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.adt.MyIList;
import model.adt.MyIFileTable;
import model.adt.IHeap;
import model.statements.IStmt;
import model.values.Value;
import model.exceptions.MyException;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Repository implements IRepository {
    private List<PrgState> prgList;
    private final String logFilePath;

    public Repository(List<PrgState> prgList, String logFilePath) {
        this.prgList = new java.util.ArrayList<>(prgList);
        this.logFilePath = logFilePath;
        // Clear the log file at initialization
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, false))) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Warning: Could not initialize log file: " + e.getMessage());
        }
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgList;
    }

    @Override
    public void setPrgList(List<PrgState> list) {
        this.prgList = list;
    }

    @Override
    public void addPrgState(PrgState prg) {
        prgList.add(prg);
    }

    @Override
    public void logPrgStateExec(PrgState prg) throws MyException {
        if (prg == null) {
            throw new MyException("No program state to log");
        }

        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            // Print program id first (concurrent requirement)
            logFile.println("PrgState ID: " + prg.getId());

            // ExeStack - print from top to bottom
            logFile.println("ExeStack:");
            MyIStack<IStmt> stk = prg.getStk();
            if (stk == null || stk.isEmpty()) {
                logFile.println("(empty)");
            } else {
                List<IStmt> stackContent = stk.getAll();
                for (int i = stackContent.size() - 1; i >= 0; i--) {
                    logFile.println(stackContent.get(i));
                }
            }

            // SymTable
            logFile.println("SymTable:");
            MyIDictionary<String, Value> symTable = prg.getSymTable();
            if (symTable == null) {
                logFile.println("(null)");
            } else {
                Map<String, Value> symContent = symTable.getContent();
                if (symContent.isEmpty()) {
                    logFile.println("(empty)");
                } else {
                    for (Map.Entry<String, Value> entry : symContent.entrySet()) {
                        logFile.println(entry.getKey() + " --> " + entry.getValue());
                    }
                }
            }

            // Out
            logFile.println("Out:");
            MyIList<Value> out = prg.getOut();
            if (out == null) {
                logFile.println("(null)");
            } else {
                List<Value> outContent = out.getAll();
                if (outContent.isEmpty()) {
                    logFile.println("(empty)");
                } else {
                    for (Value val : outContent) {
                        logFile.println(val);
                    }
                }
            }

            // FileTable
            logFile.println("FileTable:");
            MyIFileTable<?, ?> fileTable = prg.getFileTable();
            if (fileTable == null) {
                logFile.println("(null)");
            } else {
                Map<?, ?> fileContent = fileTable.getContent();
                if (fileContent.isEmpty()) {
                    logFile.println("(empty)");
                } else {
                    for (Object key : fileContent.keySet()) {
                        logFile.println(key.toString());
                    }
                }
            }

            // Heap
            logFile.println("Heap:");
            IHeap heap = prg.getHeap();
            if (heap == null) {
                logFile.println("(null)");
            } else {
                Map<Integer, Value> heapContent = heap.getContent();
                if (heapContent == null || heapContent.isEmpty()) {
                    logFile.println("(empty)");
                } else {
                    for (Map.Entry<Integer, Value> entry : heapContent.entrySet()) {
                        logFile.println(entry.getKey() + " --> " + entry.getValue());
                    }
                }
            }


            logFile.println("========================================");
            logFile.println();
        } catch (IOException e) {
            throw new MyException("Logging failed: " + e.getMessage());
        }
    }
}
