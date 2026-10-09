package model.statements;

import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyDictionary;
import model.adt.MyIStack;
import model.exceptions.ADTException;
import model.exceptions.MyException;
import model.expressions.Exp;
import model.expressions.RelExp;
import model.expressions.VarExp;
import model.types.IntType;
import model.types.Type;

import java.util.Map;

/**
 * for(v=exp1; v<exp2; v=exp3) stmt
 * Transforms to: int v; v=exp1; (while(v<exp2) stmt; v=exp3)
 */
public class ForStmt implements IStmt {
    private final String varName;
    private final Exp exp1;
    private final Exp exp2;
    private final Exp exp3;
    private final IStmt body;

    public ForStmt(String varName, Exp exp1, Exp exp2, Exp exp3, IStmt body) {
        this.varName = varName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.body = body;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (state == null) throw new MyException("For: null program state");

        // build: int v; v=exp1; (while(v<exp2) (stmt; v=exp3))
        IStmt decl = new VarDeclStmt(varName, new IntType());
        IStmt init = new AssignStmt(varName, exp1);
        // condition v < exp2
        RelExp cond = new RelExp("<", new VarExp(varName), exp2);
        // increment v = exp3
        IStmt increment = new AssignStmt(varName, exp3);
        // while body: stmt; increment
        IStmt whileBody = new CompStmt(body, increment);
        IStmt whileStmt = new WhileStmt(cond, whileBody);
        IStmt full = new CompStmt(decl, new CompStmt(init, whileStmt));

        MyIStack<IStmt> stk = state.getStk();
        stk.push(full);
        return null;
    }

    @Override
    public String toString() {
        return "for(" + varName + "=" + exp1 + "; " + varName + "<" + exp2 + "; " + varName + "=" + exp3 + ") " + body;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // verify exp1, exp2, exp3 are ints
        // We must typecheck the expressions in an environment where varName is declared as int,
        // because exp3 (and possibly exp1/exp2/body) can reference the loop variable.
        MyIDictionary<String, Type> copy = new MyDictionary<>();
        try {
            for (Map.Entry<String, Type> e : typeEnv.getContent().entrySet()) {
                copy.put(e.getKey(), e.getValue());
            }
            copy.put(varName, new IntType());
        } catch (ADTException e) {
            throw new MyException("For typecheck: error cloning type environment: " + e.getMessage());
        }

        Type t1 = exp1.typecheck(copy);
        Type t2 = exp2.typecheck(copy);
        Type t3 = exp3.typecheck(copy);
        if (!t1.equals(new IntType())) throw new MyException("For: exp1 is not an int");
        if (!t2.equals(new IntType())) throw new MyException("For: exp2 is not an int");
        if (!t3.equals(new IntType())) throw new MyException("For: exp3 is not an int");

        // typecheck the body in the same environment (where v is declared)
        body.typecheck(copy);
        return typeEnv;
    }
}
