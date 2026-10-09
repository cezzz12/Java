package test;

import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.types.RefType;
import model.types.Type;
import model.value.IntegerValue;
import repository.ListRepository;
import repository.Repository;

public class TestHeap {
    public static void testHeapBasic() {
        // Ref int v;
        Statement declareV = new VariableDeclarationStatement(new RefType(Type.INTEGER), "v");
        
        // new(v,20);
        Statement allocV = new HeapAllocStatement("v", new ValueExpression(new IntegerValue(20)));
        
        // print(rH(v));
        Statement print1 = new PrintStatement(new HeapReadExpression(new VariableExpression("v")));
        
        // wH(v,30);
        Statement writeHeap = new HeapWriteStatement("v", new ValueExpression(new IntegerValue(30)));
        
        // print(rH(v)+5);
        Statement print2 = new PrintStatement(
            new ArithmeticExpression(
                new HeapReadExpression(new VariableExpression("v")),
                new ValueExpression(new IntegerValue(5)),'+')
        );
        
        Statement program = new CompoundStatement(declareV,
                new CompoundStatement(allocV,
                        new CompoundStatement(print1,
                                new CompoundStatement(writeHeap, print2))));
        
        Repository repository = new ListRepository("log_heap_test.txt");
        Controller controller = new Controller(repository);
        
        try {
            controller.runAll(program);
            System.out.println("Program executed successfully!");
            System.out.println("Expected: Heap={1->30}, SymTable={v->(1,int)}, Out={20, 35}");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testHeapError() {
        // Ref int v;
        Statement declareV = new VariableDeclarationStatement(new RefType(Type.INTEGER), "v");
        
        // new(v,20);
        Statement allocV = new HeapAllocStatement("v", new ValueExpression(new IntegerValue(20)));
        
        // Ref Ref int a;
        Statement declareA = new VariableDeclarationStatement(
            new RefType(new RefType(Type.INTEGER)), "a");
        
        // new(a,v);
        Statement allocA = new HeapAllocStatement("a", new VariableExpression("v"));
        
        // new(v,30);
        Statement allocV2 = new HeapAllocStatement("v", new ValueExpression(new IntegerValue(30)));
        
        // print(rH(rH(a)))
        Statement print = new PrintStatement(
            new HeapReadExpression(
                new HeapReadExpression(new VariableExpression("a"))));
        
        Statement program = new CompoundStatement(declareV,
                new CompoundStatement(allocV,
                        new CompoundStatement(declareA,
                                new CompoundStatement(allocA,
                                        new CompoundStatement(allocV2, print)))));
        
        Repository repository = new ListRepository("log_heap_error_test.txt");
        Controller controller = new Controller(repository);
        
        try {
            controller.runAll(program);
            System.out.println("Program executed successfully!");
        } catch (Exception e) {
            System.out.println("Expected error occurred: " + e.getMessage());
            System.out.println("This error is expected because rH(rH(a)) tries to read from an invalid reference.");
            e.printStackTrace();
        }
    }
    
    public static void testHeapRefToRef() {
        // Ref int v;
        Statement declareV = new VariableDeclarationStatement(new RefType(Type.INTEGER), "v");
        
        // new(v,20);
        Statement allocV = new HeapAllocStatement("v", new ValueExpression(new IntegerValue(20)));
        
        // Ref Ref int a;
        Statement declareA = new VariableDeclarationStatement(
            new RefType(new RefType(Type.INTEGER)), "a");
        
        // new(a,v);
        Statement allocA = new HeapAllocStatement("a", new VariableExpression("v"));
        
        // print(v);
        Statement printV = new PrintStatement(new VariableExpression("v"));
        
        // print(a);
        Statement printA = new PrintStatement(new VariableExpression("a"));
        
        Statement program = new CompoundStatement(declareV,
                new CompoundStatement(allocV,
                        new CompoundStatement(declareA,
                                new CompoundStatement(allocA,
                                        new CompoundStatement(printV, printA)))));
        
        Repository repository = new ListRepository("log_heap_ref_alloc.txt");
        Controller controller = new Controller(repository);
        
        try {
            controller.runAll(program);
            System.out.println("Program executed successfully!");
            System.out.println("Expected: Heap={1->20, 2->(1,Ref int)}, SymTable={v->(1,int), a->(2,Ref int)}, Out={(1,int), (2,Ref int)}");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testHeapRead() {
        // Ref int v;
        Statement declareV = new VariableDeclarationStatement(new RefType(Type.INTEGER), "v");
        
        // new(v,20);
        Statement allocV = new HeapAllocStatement("v", new ValueExpression(new IntegerValue(20)));
        
        // Ref Ref int a;
        Statement declareA = new VariableDeclarationStatement(
            new RefType(new RefType(Type.INTEGER)), "a");
        
        // new(a,v);
        Statement allocA = new HeapAllocStatement("a", new VariableExpression("v"));
        
        // print(rH(v));
        Statement printReadV = new PrintStatement(
            new HeapReadExpression(new VariableExpression("v")));
        
        // print(rH(rH(a))+5);
        Statement printReadA = new PrintStatement(
            new ArithmeticExpression(
                new HeapReadExpression(
                    new HeapReadExpression(new VariableExpression("a"))),
                new ValueExpression(new IntegerValue(5)),
                '+'));
        
        Statement program = new CompoundStatement(declareV,
                new CompoundStatement(allocV,
                        new CompoundStatement(declareA,
                                new CompoundStatement(allocA,
                                        new CompoundStatement(printReadV, printReadA)))));
        
        Repository repository = new ListRepository("log_heap_read_test.txt");
        Controller controller = new Controller(repository);
        
        try {
            controller.runAll(program);
            System.out.println("Program executed successfully!");
            System.out.println("Expected: Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)}, Out={20, 25}");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Test 1: Basic Heap Operations ===");
        testHeapBasic();
        
        System.out.println("\n=== Test 2: Heap Error Case ===");
        testHeapError();
        
        System.out.println("\n=== Test 3: Heap Reference to Reference ===");
        testHeapRefToRef();
        
        System.out.println("\n=== Test 4: Heap Read Operations ===");
        testHeapRead();
    }
}
