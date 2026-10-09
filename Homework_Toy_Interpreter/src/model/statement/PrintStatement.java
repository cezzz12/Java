package model.statement;

import model.expression.Expression;
import model.types.Type;
import model.value.Value;
import state.ProgramState;

import java.util.Dictionary;
import java.util.Map;

public record PrintStatement(Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = expression.evaluate(state.symbolTable(), state.heap());
        state.out().append(value);

        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        expression.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Print(" + expression.toString()+");";
    }
}
