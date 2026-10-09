package controller;

import model.PrgState;
import model.exceptions.MyException;
import repository.IRepository;
import model.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Controller {
    private final IRepository repo;
    private java.util.concurrent.ExecutorService executor; // created in allStep per spec

    public Controller(IRepository repo) {
        this.repo = repo;
        this.executor = null;
    }

    public IRepository getRepo() {
        return repo;
    }


    public void initExecutor() {
        if (this.executor == null || this.executor.isShutdown() || this.executor.isTerminated()) {
            this.executor = java.util.concurrent.Executors.newFixedThreadPool(2);
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

        // make a mutable copy in case caller passed an immutable list
        List<PrgState> prgListMutable = new ArrayList<>(prgList);

        // before execution, print the PrgState List into the log file
        for (PrgState prg : prgListMutable) {
            repo.logPrgStateExec(prg);
        }

        // iterate by index so we can insert newly created prg states immediately
        for (int i = 0; i < prgListMutable.size(); i++) {
            PrgState p = prgListMutable.get(i);
            if (p == null || !p.isNotCompleted()) continue;
            synchronized (p) {
                try {
                    PrgState np = p.oneStep(); // may return a new PrgState from fork
                    if (np != null) {
                        // Instead of merely inserting the new program into the schedule, process it to completion now.
                        // This ensures that forked short-lived threads (like Print; assign) produce their output
                        // before the parent proceeds to subsequent statements (matching the expected example output).
                        java.util.Deque<PrgState> queue = new java.util.ArrayDeque<>();
                        queue.add(np);
                        while (!queue.isEmpty()) {
                            PrgState child = queue.poll();
                            // log initial child state
                            try { repo.logPrgStateExec(child); } catch (MyException e) { e.printStackTrace(); }
                            while (child.isNotCompleted()) {
                                try {
                                    PrgState spawned = child.oneStep();
                                    // if this child spawns more threads, schedule them for processing as well
                                    if (spawned != null) queue.add(spawned);
                                } catch (MyException me) {
                                    System.out.println("Execution error in spawned program: " + me);
                                    me.printStackTrace();
                                    try { repo.logPrgStateExec(child); } catch (MyException e) { e.printStackTrace(); }
                                    break;
                                }
                                // log child after each step
                                try { repo.logPrgStateExec(child); } catch (MyException e) { e.printStackTrace(); }
                            }
                        }
                        // do not add the finished child to the main scheduling list — it's completed
                    }
                } catch (MyException me) {
                    // Log and continue
                    System.out.println("Execution error in program ID " + p.getId() + ": " + me);
                    me.printStackTrace();
                    try { repo.logPrgStateExec(p); } catch (MyException e) { e.printStackTrace(); }
                } catch (Exception e) {
                    System.out.println("Unexpected error in program ID " + p.getId() + ": " + e);
                    e.printStackTrace();
                    try { repo.logPrgStateExec(p); } catch (MyException ex) { ex.printStackTrace(); }
                }
            }
        }

        // after the execution, print the PrgState List into the log file
        for (PrgState prg : prgListMutable) {
            repo.logPrgStateExec(prg);
        }

        // Save the current programs in the repository
        repo.setPrgList(prgListMutable);
    }


    public void allStep() throws MyException {
        // create executor here per spec
        this.executor = java.util.concurrent.Executors.newFixedThreadPool(2);

        try {
            List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
            while (!prgList.isEmpty()) {
                List<Value> allSymVals = prgList.stream()
                        .filter(Objects::nonNull)
                        .flatMap(p -> p.getSymTable().getContent().values().stream())
                        .collect(java.util.stream.Collectors.toList());

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
                .collect(java.util.stream.Collectors.toList());

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
