package controller;

import model.statement.Statement;
import model.value.Value;
import repository.Repository;
import state.*;
import state.exceptions.EmptyStackException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final Repository repository;
    ExecutorService executorService;
    public Controller(Repository repository) {
        this.repository = repository;
    }

    public void allStep() throws InterruptedException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programList = removeCompletedPrograms(repository.getProgramStates());
        while(!programList.isEmpty()){
            GarbageCollectorMap garbageCollectorMap = new GarbageCollectorMap();
            List<Value> allSymTableValues = programList.stream()
                    .flatMap(ps -> ps.symbolTable().getContents().values().stream())
                    .collect(Collectors.toList());
            List<Integer> symTableAddresses = garbageCollectorMap.getAddressesFromSymTable(allSymTableValues);

            if (!programList.isEmpty()) {
                Heap heap = programList.get(0).heap();
                Map<Integer, Value> newHeapMap = garbageCollectorMap.safeGarbageCollector(
                        symTableAddresses,
                        heap.getHeapMap()
                );
                heap.setContent(newHeapMap);
            }

            oneStepForAllPrograms(programList);
            programList = removeCompletedPrograms(programList);
        }
        executorService.shutdown();
        repository.setProgramList(programList);
    }



    public void oneStepForAllPrograms() throws InterruptedException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programList = removeCompletedPrograms(repository.getProgramStates());
        if (!programList.isEmpty()) {
            GarbageCollectorMap garbageCollectorMap = new GarbageCollectorMap();
            List<Value> allSymTableValues = programList.stream()
                    .flatMap(ps -> ps.symbolTable().getContents().values().stream())
                    .collect(Collectors.toList());
            List<Integer> symTableAddresses = garbageCollectorMap.getAddressesFromSymTable(allSymTableValues);

            Heap heap = programList.get(0).heap();
            Map<Integer, Value> newHeapMap = garbageCollectorMap.safeGarbageCollector(
                    symTableAddresses,
                    heap.getHeapMap()
            );
            heap.setContent(newHeapMap);

            oneStepForAllPrograms(programList);
            repository.setProgramList(programList);
            programList = removeCompletedPrograms(repository.getProgramStates());
        }
        executorService.shutdown();
        repository.setProgramList(programList);
    }

    public List<ProgramState> getProgramStates() {
        return repository.getProgramStates();
    }

    public void oneStepForAllPrograms(List<ProgramState> programStates) throws InterruptedException {
        programStates.forEach(repository::logProgramStateExecution);

        List<Callable<ProgramState>> callList =
                programStates.stream()
                    .map(ps -> (Callable<ProgramState>) ps::oneStep)
                    .toList();

        List<ProgramState> newProgramsList = executorService.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        IO.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        programStates.addAll(newProgramsList);
        programStates.forEach(repository::logProgramStateExecution);
        repository.setProgramList(programStates);
    }
    public void addProgramState(ProgramState programState){
        repository.addProgramState(programState);
    }

    public void removeProgramState(ProgramState programState){
        repository.removeProgramState(programState);
    }

    public void runAll(Statement statement) throws InterruptedException {
        ExecutionStack executionStack = new ListExecutionStack();
        executionStack.push(statement);
        ProgramState programState = new ProgramState(
                executionStack,
                new MapSymbolTable(),
                new ListOut(),
                new MapFileTable(),
                new HeapMap());
        addProgramState(programState);
        allStep();
        //removeProgramState(programState);
    }
    List<ProgramState> removeCompletedPrograms(List<ProgramState> programStates){
        return programStates.stream().filter(ps -> !ps.executionStack().isEmpty()).collect(Collectors.toList());
    }
}
