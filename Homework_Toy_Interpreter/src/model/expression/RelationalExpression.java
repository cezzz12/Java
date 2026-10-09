package model.expression;

import model.exception.InvalidTypeException;
import model.exception.UnknownOperatorException;
import model.types.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;
import state.Heap;
import state.SymbolTable;

import java.util.Dictionary;
import java.util.Map;

public record RelationalExpression(Expression leftOperand, Expression rightOperand, String operator)
implements Expression{
    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value leftValue = leftOperand.evaluate(symbolTable, heap);
        if (!(leftValue instanceof IntegerValue(int leftInteger)))
            throw new InvalidTypeException("Type mismatch");

        Value rightValue = rightOperand.evaluate(symbolTable,  heap);
        if (!(rightValue instanceof IntegerValue(int rightInteger)))
            throw new InvalidTypeException("Type mismatch");

        boolean result = switch (operator) {
            case "<" -> leftInteger < rightInteger;
            case "<=" -> leftInteger <= rightInteger;
            case "==" -> leftInteger == rightInteger;
            case "!=" -> leftInteger != rightInteger;
            case ">" -> leftInteger > rightInteger;
            case ">=" -> leftInteger >= rightInteger;
            default -> throw new UnknownOperatorException();
        };

        return new BooleanValue(result);
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws Exception {
        Type type1, type2;
        type1 = leftOperand.typecheck(typeEnv);
        type2 = rightOperand.typecheck(typeEnv);
        if (type1.equals(new Type.Integer())){
            if (type2.equals(new Type.Integer())){
                return new Type.Integer();
            }
            else throw new InvalidTypeException("second operand is not an integer");

        } else throw new InvalidTypeException("first operand is not an integer");
    }

    @Override
    public String toString() {
        return leftOperand.toString() + " " + operator + " " + rightOperand.toString();
    }
}
