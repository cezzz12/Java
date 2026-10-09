package test;


import model.types.Type;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import controller.Controller;
import model.statement.*;
import model.expression.Expression;
import model.value.StringValue;
import repository.ListRepository;
import repository.Repository;

public class Test {
    public void testFileOpening(){
        // string varf;
        // varf="test.in";
        Statement declareVarf = new VariableDeclarationStatement(Type.STRING, "varf");
        Expression varfExpr = new ValueExpression(new StringValue("src/test/test.in"));
        Statement assignVarf = new AssignmentStatement("varf", varfExpr);
        
        // openRFile(varf);
        Statement openFile = new OpenRFile(new VariableExpression("varf"));
        
        // int varc;
        Statement declareVarc = new VariableDeclarationStatement(Type.INTEGER, "varc");
        
        // readFile(varf,varc);print(varc);
        Statement readFile1 = new ReadFile(new VariableExpression("varf"), "varc");
        Statement print1 = new PrintStatement(new VariableExpression("varc"));
        
        // readFile(varf,varc);print(varc)
        Statement readFile2 = new ReadFile(new VariableExpression("varf"), "varc");
        Statement print2 = new PrintStatement(new VariableExpression("varc"));
        
        // closeRFile(varf)
        Statement closeFile = new CloseRFile(new VariableExpression("varf"));
        
        Statement program = new CompoundStatement(declareVarf,
                new CompoundStatement(assignVarf,
                        new CompoundStatement(openFile,
                                new CompoundStatement(declareVarc,
                                        new CompoundStatement(readFile1,
                                                new CompoundStatement(print1,
                                                        new CompoundStatement(readFile2,
                                                                new CompoundStatement(print2, closeFile))))))));
        
        Repository repository = new ListRepository("test_read_close.txt");
        Controller controller = new Controller(repository);
        
        try {
            controller.runAll(program);
            System.out.println("Program executed successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
    }
    
    public static void main(String[] args) {
        Test test = new Test();
        test.testFileOpening();
    }
}
