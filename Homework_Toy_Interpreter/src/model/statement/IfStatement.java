package model.statement;

import model.exception.InvalidTypeException;
import model.expression.Expression;
import model.types.Type;
import model.value.BooleanValue;
import model.value.Value;
import state.ProgramState;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public record IfStatement
        (Expression condition, Statement thenStatement, Statement elseStatement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = condition.evaluate(state.symbolTable(), state.heap());
        if (!(value instanceof BooleanValue(boolean booleanValue))) {
            throw new InvalidTypeException("Type mismatch");
        }

        Statement chosenStatement = booleanValue ? thenStatement : elseStatement;

        state.executionStack().push(chosenStatement);

        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        Type typeexp = condition.typecheck(typeEnv);
        if(typeexp.equals(new Type.Boolean())){
            thenStatement.typeCheck(new HashMap<>(typeEnv));
            elseStatement.typeCheck(new HashMap<>(typeEnv));
            return typeEnv;
        }
        else throw new InvalidTypeException("Type mismatch in if statement");
    }

    @Override
    public String toString() {
        return "If: " + condition.toString() + " then " + thenStatement.toString() + " else " + elseStatement.toString();
    }
}
