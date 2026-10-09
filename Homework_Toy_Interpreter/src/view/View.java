package view;

import controller.Controller;
import model.types.Type;
import model.expression.ArithmeticExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.*;
import model.value.BooleanValue;
import model.value.IntegerValue;

import java.util.Scanner;

public class View {
    Controller controller;
    public View(Controller controller) {
        this.controller = controller;
    }
    public void displayMenu(){
        IO.println("0. Exit program");
        IO.println("1. Example 1");
        IO.println("2. Example 2");
        IO.println("3. Example 3");
    }
    public void run(){
        String choice;
        do {
            displayMenu();
            IO.println("Choose Option: ");
            Scanner input = new Scanner(System.in);
            choice = input.nextLine().trim();
            switch (choice) {
                case "0":
                    break;
                case "1":
                    try {
                        Statement ex1 = new CompoundStatement(
                                new VariableDeclarationStatement(Type.INTEGER, "v"),
                                new CompoundStatement(new AssignmentStatement("v",
                                        new ValueExpression(new IntegerValue(2))),
                                        new PrintStatement(new VariableExpression("v")))
                        );
                        controller.runAll(ex1);
                    }
                    catch (Exception e) {
                        IO.println(e.getMessage());
                    }
                    break;
                case "2":
                    try{
                        Statement ex2 = new CompoundStatement( new VariableDeclarationStatement(Type.INTEGER,"a"),
                                new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER,"b"),
                                        new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression
                                                (new ValueExpression(new IntegerValue(2)),
                                                        new ArithmeticExpression(new ValueExpression(new IntegerValue(3)),
                                                                new ValueExpression(new IntegerValue(5)),'*'),'+')),
                                                new CompoundStatement(new AssignmentStatement
                                                        ("b",new ArithmeticExpression(new VariableExpression("a"), new ValueExpression
                                                                (new IntegerValue(1)),'+')), new PrintStatement(new VariableExpression("b"))))));
                        controller.runAll(ex2);
                    }
                    catch (Exception e){
                        IO.println(e.getMessage());
                    }
                    break;
                case "3":
                    try{
                        Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(Type.BOOLEAN,"a"),
                                new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER,"v"),
                                        new CompoundStatement(new AssignmentStatement
                                                ("a", new ValueExpression(new BooleanValue(true))),
                                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                                        new AssignmentStatement
                                                                ("v",new ValueExpression(new IntegerValue(2))),
                                                        new AssignmentStatement("v", new ValueExpression
                                                                (new IntegerValue(3)))),
                                                        new PrintStatement(new VariableExpression("v"))))));
                        controller.runAll(ex3);
                    }
                    catch (Exception e){
                        IO.println(e.getMessage());
                    }
                    break;
                default:
                    IO.println("Invalid choice");


            }
        } while (!choice.equals("0"));
    }
}
