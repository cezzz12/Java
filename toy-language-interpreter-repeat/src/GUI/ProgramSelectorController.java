package GUI;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.PrgState;
import model.adt.*;

import model.statements.CompStmt;
import model.statements.VarDeclStmt;
import model.statements.PrintStmt;
import model.statements.IfStmt;
import model.statements.OpenRFileStmt;
import model.statements.ReadFileStmt;
import model.statements.CloseRFileStmt;
import model.statements.NewStmt;
import model.statements.ForkStmt;
import model.statements.WriteHeapStmt;
import model.statements.IStmt;
import model.statements.AssignStmt;
import model.statements.RepeatStmt;
import model.statements.NopStmt;

import model.expressions.ValueExp;
import model.expressions.VarExp;
import model.expressions.ArithExp;
import model.expressions.ReadHeapExpr;
import model.expressions.RelExp;

import model.types.IntType;
import model.types.BoolType;
import model.types.StringType;
import model.types.RefType;
import model.types.Type;

import model.values.IntValue;
import model.values.BoolValue;
import model.values.StringValue;

import repository.Repository;
import repository.IRepository;
import model.exceptions.MyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ProgramSelectorController {
    @FXML
    @SuppressWarnings("unused")
    private ListView<String> programsList;

    private final List<Controller> controllers = new ArrayList<>();

    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        // Build the same examples as the CLI `Main` so GUI matches
        buildExamples();

        List<String> items = new ArrayList<>();
        for (Controller c : controllers) {
            if (c == null) items.add("[Invalid program: type error]");
            else {
                PrgState p = c.getRepo().getPrgList().get(0);
                items.add(p.getOriginalProgram().toString());
            }
        }
        programsList.getItems().setAll(items);
        if (!items.isEmpty()) programsList.getSelectionModel().selectFirst();
    }

    @FXML
    @SuppressWarnings("unused")
    public void handleOpen() {
        int idx = programsList.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        Controller ctr = controllers.get(idx);
        if (ctr == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Interpreter.fxml"));
            Parent root = loader.load();
            InterpreterController ic = loader.getController();
            ic.setController(ctr);

            Stage stage = new Stage();
            stage.setTitle("Interpreter");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to open interpreter window: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    public void handleExit() {
        Stage st = (Stage) programsList.getScene().getWindow();
        st.close();
    }

    private void buildExamples() {
        controllers.clear();

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
            ex1Ok = false;
        }

        if (ex1Ok) {
            PrgState prg1 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex1
            );
            IRepository repo1 = new Repository(Collections.singletonList(prg1), "log1.txt");
            Controller ctr1 = new Controller(repo1);
            controllers.add(ctr1);
        } else controllers.add(null);

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
            ex2Ok = false;
        }

        if (ex2Ok) {
            PrgState prg2 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex2
            );
            IRepository repo2 = new Repository(Collections.singletonList(prg2), "log2.txt");
            Controller ctr2 = new Controller(repo2);
            controllers.add(ctr2);
        } else controllers.add(null);

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
            ex3Ok = false;
        }

        if (ex3Ok) {
            PrgState prg3 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex3
            );
            IRepository repo3 = new Repository(Collections.singletonList(prg3), "log3.txt");
            Controller ctr3 = new Controller(repo3);
            controllers.add(ctr3);
        } else controllers.add(null);

        // ---------------- EXAMPLE 4 — FILE TEST ----------------
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
            ex4Ok = false;
        }

        if (ex4Ok) {
            PrgState prg4 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex4
            );
            IRepository repo4 = new Repository(Collections.singletonList(prg4), "log4.txt");
            Controller ctr4 = new Controller(repo4);
            controllers.add(ctr4);
        } else controllers.add(null);

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
            ex5Ok = false;
        }

        if (ex5Ok) {
            PrgState prg5 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex5
            );
            IRepository repo5 = new Repository(Collections.singletonList(prg5), "log5.txt");
            Controller ctr5 = new Controller(repo5);
            controllers.add(ctr5);
        } else controllers.add(null);

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
            ex6Ok = false;
        }

        if (ex6Ok) {
            PrgState prg6 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex6
            );
            IRepository repo6 = new Repository(Collections.singletonList(prg6), "log6.txt");
            Controller ctr6 = new Controller(repo6);
            controllers.add(ctr6);
        } else controllers.add(null);

        // ---------------- EXAMPLE 7 — REPEAT / FORK test ----------------
        IStmt ex7 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("x", new IntType()),
                        new CompStmt(
                                new VarDeclStmt("y", new IntType()),
                                new CompStmt(
                                        new AssignStmt("v", new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new RepeatStmt(
                                                        new CompStmt(
                                                                new ForkStmt(new CompStmt(
                                                                        new PrintStmt(new VarExp("v")),
                                                                        new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1))))
                                                                )),
                                                                new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))))
                                                        ),
                                                        new RelExp("==", new VarExp("v"), new ValueExp(new IntValue(3)))
                                                ),
                                                new CompStmt(
                                                        new AssignStmt("x", new ValueExp(new IntValue(1))),
                                                        new CompStmt(
                                                                new NopStmt(),
                                                                new CompStmt(
                                                                        new AssignStmt("y", new ValueExp(new IntValue(3))),
                                                                        new CompStmt(
                                                                                new NopStmt(),
                                                                                new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10))))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        boolean ex7Ok = true;
        try {
            MyIDictionary<String, Type> typeEnv7 = new MyDictionary<>();
            ex7.typecheck(typeEnv7);
        } catch (MyException e) {
            ex7Ok = false;
        }

        if (ex7Ok) {
            PrgState prg7 = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    ex7
            );
            IRepository repo7 = new Repository(Collections.singletonList(prg7), "log7.txt");
            Controller ctr7 = new Controller(repo7);
            controllers.add(ctr7);
        } else controllers.add(null);

    }
}
