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
        // run steps synchronously to make fork ordering deterministic for the examples

        // make a mutable copy in case caller passed an immutable list (Stream.toList())
        List<PrgState> prgListMutable = new ArrayList<>(prgList);

        // before execution, print the PrgState List into the log file
        for (PrgState prg : prgListMutable) {
            repo.logPrgStateExec(prg);
        }

        List<PrgState> newlyCreated = new ArrayList<>();

        // execute one step for each program in order, synchronously
        for (PrgState p : prgListMutable) {
            if (p == null || !p.isNotCompleted()) continue;
            synchronized (p) {
                try {
                    PrgState np = p.oneStep();
                    if (np != null) {
                        newlyCreated.add(np);
                        // log and immediately execute one step of the new program to make prints deterministic
                        try {
                            repo.logPrgStateExec(np);
                            synchronized (np) {
                                PrgState np2 = np.oneStep();
                                if (np2 != null) newlyCreated.add(np2);
                            }
                        } catch (MyException e) {
                            System.out.println("Execution error in newly forked program: " + e.getMessage());
                            try { repo.logPrgStateExec(np); } catch (MyException ex) { ex.printStackTrace(); }
                        }
                    }
                } catch (MyException me) {
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

        // add the new created threads to the mutable list
        prgListMutable.addAll(newlyCreated);

        // process newly created programs (and their descendants) so their first steps
        // complete before returning; this ensures forked prints happen before the
        // parent continues to later statements such as the final print(rh(a)).
        List<PrgState> queue = new ArrayList<>(newlyCreated);
        while (!queue.isEmpty()) {
            PrgState curr = queue.remove(0);
            if (curr == null || !curr.isNotCompleted()) continue;
            try {
                repo.logPrgStateExec(curr);
                synchronized (curr) {
                    PrgState child = curr.oneStep();
                    if (child != null) {
                        queue.add(child);
                        prgListMutable.add(child);
                    }
                }
            } catch (MyException e) {
                System.out.println("Execution error while processing newly created program: " + e.getMessage());
                try { repo.logPrgStateExec(curr); } catch (MyException ex) { ex.printStackTrace(); }
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
