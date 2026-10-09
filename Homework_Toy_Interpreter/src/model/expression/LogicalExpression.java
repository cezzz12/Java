package model.expression;

import model.exception.InvalidTypeException;
import model.exception.UnknownOperatorException;
import model.types.Type;
import model.value.BooleanValue;
import model.value.Value;
import state.Heap;
import state.SymbolTable;

import java.util.Dictionary;
import java.util.Map;

public record LogicalExpression(
        Expression leftOperand,
        Expression rightOperand,
        String operator
) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value leftValue = leftOperand.evaluate(symbolTable, heap);
        if (!(leftValue instanceof BooleanValue(boolean leftBool)))
            throw new InvalidTypeException("Type mismatch");

        Value rightValue = rightOperand.evaluate(symbolTable, heap);
        if (!(rightValue instanceof BooleanValue(boolean rightBool)))
            throw new InvalidTypeException("Type mismatch");

        boolean result = switch (operator) {
            case "&&" -> leftBool && rightBool;
            case "||" -> leftBool || rightBool;
            default -> throw new UnknownOperatorException();
        };

        return new BooleanValue(result);
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws Exception {
        Type type1, type2;
        type1 = leftOperand.typecheck(typeEnv);
        type2 = rightOperand.typecheck(typeEnv);
        if (type1.equals(new Type.Boolean())){
            if (type2.equals(new Type.Boolean())){
                return new Type.Boolean();
            }
            else throw new InvalidTypeException("second operand is not an boolean");

        } else throw new InvalidTypeException("first operand is not an boolean");
    }

    @Override
    public String toString() {
        return leftOperand.toString() + " " + operator + " " + rightOperand.toString();
    }
}
