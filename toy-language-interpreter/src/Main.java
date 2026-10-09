import controller.Controller;
import model.PrgState;
import model.adt.MyStack;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import model.values.*;
import repository.Repository;
import repository.IRepository;
import view.*;
import model.exceptions.MyException;
import model.adt.MyIDictionary;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {

        // ---------------- EXAMPLE 1 ----------------
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
        boolean ex1Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv1 = new MyDictionary<>();
            ex1.typecheck(typeEnv1);
        } catch (MyException e) {
            System.out.println("Typecheck failed for example 1: " + e.getMessage());
            ex1Ok = false;
        }

        PrgState prg1 = null; IRepository repo1 = null; Controller ctr1 = null;
        if (ex1Ok) {
            prg1 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex1
            );
            repo1 = new Repository(Collections.singletonList(prg1), "log1.txt");
            ctr1 = new Controller(repo1);
        }


        // ---------------- EXAMPLE 2 ----------------
        IStmt ex2 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ArithExp(
                                        '+',
                                        new ValueExp(new IntValue(2)),
                                        new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5)))
                                )),
                                new CompStmt(
                                        new AssignStmt("b", new ArithExp(
                                                '+',
                                                new VarExp("a"),
                                                new ValueExp(new IntValue(1))
                                        )),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );
        boolean ex2Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv2 = new MyDictionary<>();
            ex2.typecheck(typeEnv2);
        } catch (MyException e) {
            System.out.println("Typecheck failed for example 2: " + e.getMessage());
            ex2Ok = false;
        }

        PrgState prg2 = null; IRepository repo2 = null; Controller ctr2 = null;
        if (ex2Ok) {
            prg2 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex2
            );
            repo2 = new Repository(Collections.singletonList(prg2), "log2.txt");
            ctr2 = new Controller(repo2);
        }


        // ---------------- EXAMPLE 3 ----------------
        IStmt ex3 = new CompStmt(
                new VarDeclStmt("a", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
        boolean ex3Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv3 = new MyDictionary<>();
            ex3.typecheck(typeEnv3);
        } catch (MyException e) {
            System.out.println("Typecheck failed for example 3: " + e.getMessage());
            ex3Ok = false;
        }

        PrgState prg3 = null; IRepository repo3 = null; Controller ctr3 = null;
        if (ex3Ok) {
            prg3 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex3
            );
            repo3 = new Repository(Collections.singletonList(prg3), "log3.txt");
            ctr3 = new Controller(repo3);
        }


        // ---------------- EXAMPLE 4 — FILE TEST ----------------
        // This corresponds to:
        // string varf;
        // varf="test.in";
        // openRFile(varf);
        // int varc;
        // readFile(varf,varc); print(varc);
        // readFile(varf,varc); print(varc);
        // closeRFile(varf);

        IStmt ex4 = new CompStmt(
                new VarDeclStmt("varf", new StringType()),
                new CompStmt(
                        new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(
                                new OpenRFileStmt(new VarExp("varf")),
                                new CompStmt(
                                        new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(
                                                new ReadFileStmt(new VarExp("varf"), "varc"),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(
                                                                new ReadFileStmt(new VarExp("varf"), "varc"),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFileStmt(new VarExp("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        boolean ex4Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv4 = new MyDictionary<>();
            ex4.typecheck(typeEnv4);
        } catch (MyException e) {
            System.out.println("Typecheck failed for example 4: " + e.getMessage());
            ex4Ok = false;
        }

        PrgState prg4 = null; IRepository repo4 = null; Controller ctr4 = null;
        if (ex4Ok) {
            prg4 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex4
            );
            repo4 = new Repository(Collections.singletonList(prg4), "log4.txt");
            ctr4 = new Controller(repo4);
        }


// Java
// ---------------- EXAMPLE 5 — HEAP / GC test ----------------
        IStmt ex5 = new CompStmt(
                new VarDeclStmt("x", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("x", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("y", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("y", new VarExp("x")),
                                        new CompStmt(
                                                new NewStmt("x", new ValueExp(new IntValue(30))),
                                                new PrintStmt(
                                                        new ReadHeapExpr(
                                                                new ReadHeapExpr(new VarExp("y"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        boolean ex5Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv5 = new MyDictionary<>();
            ex5.typecheck(typeEnv5);
        } catch (MyException e) {
            System.out.println("Typecheck failed for example 5: " + e.getMessage());
            ex5Ok = false;
        }

        PrgState prg5 = null; IRepository repo5 = null; Controller ctr5 = null;
        if (ex5Ok) {
            prg5 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex5
            );
            repo5 = new Repository(Collections.singletonList(prg5), "log5.txt");
            ctr5 = new Controller(repo5);
        }


        // ---------------- EXAMPLE 6 — FORK / HEAP test ----------------
        IStmt ex6 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(new CompStmt(
                                                        new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                        new CompStmt(
                                                                new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("v")),
                                                                        new PrintStmt(new ReadHeapExpr(new VarExp("a")))
                                                                )
                                                        )
                                                )),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(new ReadHeapExpr(new VarExp("a")))
                                                )
                                        )
                                )
                        )
                )
        );

        boolean ex6Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv6 = new MyDictionary<>();
            ex6.typecheck(typeEnv6);
        } catch (MyException e) {
            System.out.println("Typecheck failed for example 6: " + e.getMessage());
            ex6Ok = false;
        }

        PrgState prg6 = null; IRepository repo6 = null; Controller ctr6 = null;
        if (ex6Ok) {
            prg6 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex6
            );
            repo6 = new Repository(Collections.singletonList(prg6), "log6.txt");
            ctr6 = new Controller(repo6);
        }


        // for example
        //  Ref int a; new(a,20);
        //  for(v=1; v<14; v=v+1) fork(print(v); v=v*readHeap(a));
        //  print(readHeap(a))
        IStmt ex7 = new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("a", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new FORstmt("v", new ValueExp(new IntValue(1)), new ValueExp(new IntValue(14)), new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))),
                                        new ForkStmt(new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new AssignStmt("v", new ArithExp('*', new VarExp("v"), new ReadHeapExpr(new VarExp("a"))))
                                        ))
                                ),
                                new PrintStmt(new ReadHeapExpr(new VarExp("a")))
                        )
                )
        );

        boolean ex7Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv7 = new MyDictionary<>();
            ex7.typecheck(typeEnv7);
        } catch (MyException e) {
            System.out.println("Typecheck failed for example 7: " + e.getMessage());
            ex7Ok = false;
        }

        PrgState prg7 = null; IRepository repo7 = null; Controller ctr7 = null;
        if (ex7Ok) {
            prg7 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex7
            );
            repo7 = new Repository(Collections.singletonList(prg7), "log7.txt");
            ctr7 = new Controller(repo7);
        }


        // ---------------- MENU ----------------
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        if (ctr1 != null) menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        if (ctr2 != null) menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        if (ctr3 != null) menu.addCommand(new RunExample("3", ex3.toString(), ctr3));
        if (ctr4 != null) menu.addCommand(new RunExample("4", ex4.toString(), ctr4));  // FILE TEST
        if (ctr5 != null) menu.addCommand(new RunExample("5", ex5.toString(), ctr5));
        if (ctr6 != null) menu.addCommand(new RunExample("6", ex6.toString(), ctr6));
        if (ctr7 != null) menu.addCommand(new RunExample("7", ex7.toString(), ctr7));
        menu.show();
    }
}
