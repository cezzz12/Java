package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exceptions.MyException;
import model.expressions.Exp;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;

/**
  for(v=exp1; v<exp2; v=exp3) stmt
         pe stiva int v; v = exp1 * 10; (while (v < exp2) { stmt; v = exp3 })
 */
public class FORstmt implements IStmt {
    private final String varName;
    private final Exp exp1;
    private final Exp exp2;
    private final Exp exp3;
    private final IStmt body;

    public FORstmt(String varName, Exp exp1, Exp exp2, Exp exp3, IStmt body) {
        this.varName = varName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.body = body;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();
        //  int v; v=exp1*10; while(v<exp2) { body; v=exp3 }
        IStmt declare = new VarDeclStmt(varName, new IntType());
        IStmt init = new AssignStmt(varName, new model.expressions.ArithExp('*', exp1, new model.expressions.ValueExp(new IntValue(10))));
        IStmt whileBody = new CompStmt(body, new AssignStmt(varName, exp3));
        IStmt whileStmt = new WhileStmt(new model.expressions.RelExp("<", new model.expressions.VarExp(varName), exp2), whileBody);
        IStmt newStmt = new CompStmt(declare, new CompStmt(init, whileStmt));
        stk.push(newStmt);
        return null;
    }

    @Override
    public String toString() {
        return "for(" + varName + " = " + exp1 + "; " + varName + " < " + exp2 + "; " + varName + " = " + exp3 + ") " + body;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        MyIDictionary<String, Type> newEnv = new model.adt.MyDictionary<>();
        for (String k : typeEnv.getContent().keySet()) {
            newEnv.put(k, typeEnv.lookup(k));
        }
        newEnv.put(varName, new IntType());
        Type t1 = exp1.typecheck(newEnv);
        Type t2 = exp2.typecheck(newEnv);
        Type t3 = exp3.typecheck(newEnv);
        if (!t1.equals(new IntType())) throw new MyException("For: exp1 is not an integer");
        if (!t2.equals(new IntType())) throw new MyException("For: exp2 is not an integer");
        if (!t3.equals(new IntType())) throw new MyException("For: exp3 is not an integer");
        body.typecheck(newEnv);
        return typeEnv;
    }
}
