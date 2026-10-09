package controller;

import model.PrgState;
import model.exceptions.MyException;
import repository.IRepository;
import model.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repo;
    private ExecutorService executor; // created in allStep per spec

    public Controller(IRepository repo) {
        this.repo = repo;
        this.executor = null;
    }

    public IRepository getRepo() {
        return repo;
    }


    public void initExecutor() {
        if (this.executor == null || this.executor.isShutdown() || this.executor.isTerminated()) {
            this.executor = Executors.newFixedThreadPool(2);
        }
    }


    public void shutdownExecutor() {
        if (this.executor != null) {
            this.executor.shutdownNow();
            this.executor = null;
        }
    }


    public void oneStepForAllPrg(List<PrgState> prgList) throws MyException {
        if (prgList == null) return;
        if (executor == null) throw new MyException("Executor not initialized");

        // make a mutable copy in case caller passed an immutable list (Stream.toList())
        List<PrgState> prgListMutable = new ArrayList<>(prgList);

        // before execution, print the PrgState List into the log file
        for (PrgState prg : prgListMutable) {
            repo.logPrgStateExec(prg);
        }

        // prepare the list of callables only for programs that are not completed
        List<PrgState> toRun = prgListMutable.stream().filter(p -> p != null && p.isNotCompleted()).toList();

        List<Callable<PrgState>> callList = toRun.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> {
                    // synchronize on the program to avoid concurrent modifications of the same PrgState
                    synchronized (p) {
                        if (!p.isNotCompleted()) return null;
                        try {
                            return p.oneStep();
                        } catch (MyException me) {
                            // Log and return null to avoid ExecutionException
                            System.out.println("Execution error in program ID " + p.getId() + ": " + me);
                            me.printStackTrace();
                            try { repo.logPrgStateExec(p); } catch (MyException e) { e.printStackTrace(); }
                            return null;
                        } catch (Exception e) {
                            System.out.println("Unexpected error in program ID " + p.getId() + ": " + e);
                            e.printStackTrace();
                            try { repo.logPrgStateExec(p); } catch (MyException ex) { ex.printStackTrace(); }
                            return null;
                        }
                    }
                }))
                .collect(Collectors.toList());

        List<PrgState> newPrgList = new ArrayList<>();
        try {
            // start the execution of the callables
            List<Future<PrgState>> futures = executor.invokeAll(callList);

            // collect results (new PrgStates returned by fork)
            for (Future<PrgState> f : futures) {
                try {
                    PrgState np = f.get();
                    if (np != null) newPrgList.add(np);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new MyException("oneStepForAllPrg interrupted: " + e.getMessage());
                } catch (ExecutionException e) {
                    // This should be rare since callables handle their own exceptions, but handle defensively
                    Throwable cause = e.getCause();
                    System.out.println("Execution error collecting future result: " + e + ", cause: " + cause);
                    if (cause != null) cause.printStackTrace(); else e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MyException("oneStepForAllPrg invokeAll interrupted: " + e.getMessage());
        }

        // add the new created threads to the mutable list
        prgListMutable.addAll(newPrgList);

        // after the execution, print the PrgState List into the log file
        for (PrgState prg : prgListMutable) {
            repo.logPrgStateExec(prg);
        }

        // Save the current programs in the repository
        repo.setPrgList(prgListMutable);
    }


    public void allStep() throws MyException {
        // create executor here per spec
        this.executor = Executors.newFixedThreadPool(2);

        try {
            List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
            while (!prgList.isEmpty()) {
                List<Value> allSymVals = prgList.stream()
                        .filter(Objects::nonNull)
                        .flatMap(p -> p.getSymTable().getContent().values().stream())
                        .collect(Collectors.toList());

                PrgState any = prgList.get(0);
                Map<Integer, Value> newHeap = GarbageCollector.collectReachable(allSymVals, any.getHeap().getContent());
                for (PrgState p : prgList) {
                    p.getHeap().setContent(newHeap);
                }

                oneStepForAllPrg(prgList);

                prgList = removeCompletedPrg(repo.getPrgList());
            }
        } finally {
            // shutdown executor when finished (or if an exception occurred)
            if (this.executor != null) {
                this.executor.shutdownNow();
                this.executor = null;
            }
        }
    }

    // helper: remove completed programs from the list
    private List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(p -> p != null && p.isNotCompleted()).toList();
    }


    public void stepOnce() throws MyException {
        initExecutor();
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        if (prgList.isEmpty()) return;

        // conservative garbage collector before executing steps
        List<Value> allSymVals = prgList.stream()
                .filter(Objects::nonNull)
                .flatMap(p -> p.getSymTable().getContent().values().stream())
                .collect(Collectors.toList());

        PrgState any = prgList.get(0);
        Map<Integer, Value> newHeap = GarbageCollector.collectReachable(allSymVals, any.getHeap().getContent());
        for (PrgState p : prgList) {
            p.getHeap().setContent(newHeap);
        }

        oneStepForAllPrg(prgList);
    }

    // Small GUI-friendly wrapper to call stepOnce from UI code. Keeps the same semantics and exception.
    public void stepOnceGui() throws MyException {
        this.stepOnce();
    }
}
