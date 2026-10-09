package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.types.RefType;
import model.types.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import repository.ListRepository;
import repository.Repository;
import state.*;

import java.util.HashMap;
public class Interpreter extends Application { // Change to public and extend Application

    @Override
    public void start(Stage primaryStage) {
        Statement ex1 = new CompoundStatement(
                new VariableDeclarationStatement(Type.INTEGER, "v"),
                new CompoundStatement(new AssignmentStatement("v",
                        new ValueExpression(new IntegerValue(2))),
                        new PrintStatement(new VariableExpression("v")))
        );

        Statement ex2 = new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "a"),
                new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "b"),
                        new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression
                                (new ValueExpression(new IntegerValue(2)),
                                        new ArithmeticExpression(new ValueExpression(new IntegerValue(3)),
                                                new ValueExpression(new IntegerValue(5)), '*'), '+')),
                                new CompoundStatement(new AssignmentStatement
                                        ("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression
                                                (new IntegerValue(1)), '+')), new PrintStatement(new VariableExpression("b"))))));

        Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(Type.BOOLEAN, "a"),
                new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "v"),
                        new CompoundStatement(new AssignmentStatement
                                ("a", new ValueExpression(new BooleanValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignmentStatement
                                                ("v", new ValueExpression(new IntegerValue(2))),
                                        new AssignmentStatement("v", new ValueExpression
                                                (new IntegerValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));

        Statement ex4 = new CompoundStatement(
                new VariableDeclarationStatement(Type.INTEGER, "v"),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntegerValue(4))),
                        new CompoundStatement(
                                new WhileStatement(
                                        new RelationalExpression(
                                                new VariableExpression("v"),
                                                new ValueExpression(new IntegerValue(0)),
                                                ">"
                                        ),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignmentStatement("v",
                                                        new ArithmeticExpression(
                                                                new VariableExpression("v"),
                                                                new ValueExpression(new IntegerValue(1)),
                                                                '-'
                                                        )
                                                )
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v"))
                        )
                )
        );

        Statement ex5 = new CompoundStatement(
                new VariableDeclarationStatement(Type.INTEGER, "v"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(Type.INTEGER), "a"),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ValueExpression(new IntegerValue(10))),
                                new CompoundStatement(
                                        new HeapAllocStatement("a", new ValueExpression(new IntegerValue(22))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new HeapWriteStatement("a", new ValueExpression(new IntegerValue(30))),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement("v", new ValueExpression(new IntegerValue(32))),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );

        java.util.List<Statement> programs = java.util.Arrays.asList(ex1, ex2, ex3, ex4, ex5);
        SelectWindow selectWindow = new SelectWindow(programs);
        selectWindow.show();
    }

    public static void main(String[] args) {
        // Instead of menu.show(), call launch()
        launch(args);
    }
}
//class Interpreter {
//    public static void main(String[] args) {
//        Statement ex1 = new CompoundStatement(
//                new VariableDeclarationStatement(Type.INTEGER, "v"),
//                new CompoundStatement(new AssignmentStatement("v",
//                        new ValueExpression(new IntegerValue(2))),
//                        new PrintStatement(new VariableExpression("v")))
//        );
//        ExecutionStack executionStack1 = new ListExecutionStack();
//        executionStack1.push(ex1);
//        ProgramState prg1 = new ProgramState(
//                executionStack1,
//                new MapSymbolTable(),
//                new ListOut(),
//                new MapFileTable(),
//                new HeapMap());
//        Repository repo1 = new ListRepository("log1.txt");
//        repo1.addProgramState(prg1);
//        Controller ctr1 = new Controller(repo1);
//
//        Statement ex2 = new CompoundStatement( new VariableDeclarationStatement(Type.INTEGER,"a"),
//                new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER,"b"),
//                        new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression
//                                (new ValueExpression(new IntegerValue(2)),
//                                        new ArithmeticExpression(new ValueExpression(new IntegerValue(3)),
//                                                new ValueExpression(new IntegerValue(5)),'*'),'+')),
//                                new CompoundStatement(new AssignmentStatement
//                                        ("b",new ArithmeticExpression(new VariableExpression("a"), new ValueExpression
//                                                (new IntegerValue(1)),'+')), new PrintStatement(new VariableExpression("b"))))));
//
//        ExecutionStack executionStack2 = new ListExecutionStack();
//        executionStack2.push(ex2);
//        ProgramState prg2 = new ProgramState(
//                executionStack2,
//                new MapSymbolTable(),
//                new ListOut(),
//                new MapFileTable(),
//                new HeapMap());
//        Repository repo2 = new ListRepository("log2.txt");
//        repo2.addProgramState(prg2);
//        Controller ctr2 = new Controller(repo2);
//
//        Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(Type.BOOLEAN,"a"),
//                new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER,"v"),
//                        new CompoundStatement(new AssignmentStatement
//                                ("a", new ValueExpression(new BooleanValue(true))),
//                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
//                                        new AssignmentStatement
//                                                ("v",new ValueExpression(new IntegerValue(2))),
//                                        new AssignmentStatement("v", new ValueExpression
//                                                (new IntegerValue(3)))),
//                                        new PrintStatement(new VariableExpression("v"))))));
//        ExecutionStack executionStack3 = new ListExecutionStack();
//        executionStack3.push(ex3);
//        ProgramState prg3 = new ProgramState(
//                executionStack3,
//                new MapSymbolTable(),
//                new ListOut(),
//                new MapFileTable(),
//                new HeapMap());
//        Repository repo3 = new ListRepository("log3.txt");
//        repo3.addProgramState(prg3);
//
//        Controller ctr3 = new Controller(repo3);
//
//        // Example 4: While statement
//        // int v; v=4; while(v>0) {print(v); v=v-1}; print(v)
//        Statement ex4 = new CompoundStatement(
//                new VariableDeclarationStatement(Type.INTEGER, "v"),
//                new CompoundStatement(
//                        new AssignmentStatement("v", new ValueExpression(new IntegerValue(4))),
//                        new CompoundStatement(
//                                new WhileStatement(
//                                        new RelationalExpression(
//                                                new VariableExpression("v"),
//                                                new ValueExpression(new IntegerValue(0)),
//                                                ">"
//                                        ),
//                                        new CompoundStatement(
//                                                new PrintStatement(new VariableExpression("v")),
//                                                new AssignmentStatement("v",
//                                                        new ArithmeticExpression(
//                                                                new VariableExpression("v"),
//                                                                new ValueExpression(new IntegerValue(1)),
//                                                                '-'
//                                                        )
//                                                )
//                                        )
//                                ),
//                                new PrintStatement(new VariableExpression("v"))
//                        )
//                )
//        );
//
//        ExecutionStack executionStack4 = new ListExecutionStack();
//        executionStack4.push(ex4);
//        ProgramState prg4 = new ProgramState(
//                executionStack4,
//                new MapSymbolTable(),
//                new ListOut(),
//                new MapFileTable(),
//                new HeapMap());
//        Repository repo4 = new ListRepository("log4.txt");
//        repo4.addProgramState(prg4);
//        Controller ctr4 = new Controller(repo4);
//
//        // Example 5: Fork with heap operations
//        // int v; Ref int a; v=10; new(a,22);
//        // fork(wH(a,30); v=32; print(v); print(rH(a)));
//        // print(v); print(rH(a))
//        Statement ex5 = new CompoundStatement(
//                new VariableDeclarationStatement(Type.INTEGER, "v"),
//                new CompoundStatement(
//                        new VariableDeclarationStatement(new RefType(Type.INTEGER), "a"),
//                        new CompoundStatement(
//                                new AssignmentStatement("v", new ValueExpression(new IntegerValue(10))),
//                                new CompoundStatement(
//                                        new HeapAllocStatement("a", new ValueExpression(new IntegerValue(22))),
//                                        new CompoundStatement(
//                                                new ForkStatement(
//                                                        new CompoundStatement(
//                                                                new HeapWriteStatement("a", new ValueExpression(new IntegerValue(30))),
//                                                                new CompoundStatement(
//                                                                        new AssignmentStatement("v", new ValueExpression(new IntegerValue(32))),
//                                                                        new CompoundStatement(
//                                                                                new PrintStatement(new VariableExpression("v")),
//                                                                                new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
//                                                                        )
//                                                                )
//                                                        )
//                                                ),
//                                                new CompoundStatement(
//                                                        new PrintStatement(new VariableExpression("v")),
//                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
//                                                )
//                                        )
//                                )
//                        )
//                )
//        );
//        try {
//            ex1.typeCheck(new HashMap<String, Type>());
//            ex2.typeCheck(new HashMap<String, Type>());
//            ex3.typeCheck(new HashMap<String, Type>());
//            ex4.typeCheck(new HashMap<String, Type>());
//            ex5.typeCheck(new HashMap<String, Type>());
//            IO.println("All typecheckers for all examples passed");
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            IO.println(e.getMessage());
//        }
//        ExecutionStack executionStack5 = new ListExecutionStack();
//        executionStack5.push(ex5);
//        ProgramState prg5 = new ProgramState(
//                executionStack5,
//                new MapSymbolTable(),
//                new ListOut(),
//                new MapFileTable(),
//                new HeapMap());
//        Repository repo5 = new ListRepository("log5.txt");
//        repo5.addProgramState(prg5);
//        Controller ctr5 = new Controller(repo5);
//
//        TextMenu menu = new TextMenu();
//        menu.addCommand(new ExitCommand("0", "exit"));
//        menu.addCommand(new RunExample("1","First example",ctr1));
//        menu.addCommand(new RunExample("2","Second example",ctr2));
//        menu.addCommand(new RunExample("3","Third example",ctr3));
//        menu.addCommand(new RunExample("4","While example: v=4; while(v>0) print and decrement",ctr4));
//        menu.addCommand(new RunExample("5","Fork with heap: v=10; new(a,22); fork(wH(a,30); v=32; print; print); print; print",ctr5));
//        menu.show();
//    }
//}

