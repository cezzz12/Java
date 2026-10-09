package model.statement;

import model.exception.InvalidTypeException;
import model.types.Type;
import model.value.BooleanValue;
import model.value.Value;
import state.ProgramState;
import model.expression.Expression;

import java.util.Map;

public record WhileStatement(Expression expression, Statement statement) implements Statement {
    
    @Override
    public ProgramState execute(ProgramState state) {
        Value conditionValue = expression.evaluate(state.symbolTable(), state.heap());
        
        // Check if the condition evaluates to a BooleanValue
        if (!conditionValue.getType().equals(Type.BOOLEAN)) {
            throw new InvalidTypeException("Condition expression is not a boolean");
        }
        
        BooleanValue boolValue = (BooleanValue) conditionValue;
        
        if (boolValue.value()) {

            state.executionStack().push(this);
            state.executionStack().push(statement);
        }

        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        return statement.typeCheck(typeEnv);
    }

    @Override
    public String toString() {
        return "while(" + expression.toString() + ") " + statement.toString();
    }
}
