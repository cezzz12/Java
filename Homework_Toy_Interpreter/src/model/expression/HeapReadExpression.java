package model.expression;

import model.exception.InvalidTypeException;
import model.statement.Statement;
import model.types.RefType;
import model.types.Type;
import model.value.RefValue;
import model.value.Value;
import state.Heap;
import state.ProgramState;
import state.SymbolTable;
import state.exceptions.AddressNotInHeap;
import model.expression.Expression;

import java.util.Dictionary;
import java.util.Map;

public record HeapReadExpression(Expression expression) implements Expression {

    @Override
    public String toString() {
        return "HeapRead("+expression.toString()+")";
    }

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value expressionValue = expression.evaluate(symbolTable, heap);

        if (!(expressionValue.getType() instanceof RefType) ){
            throw new InvalidTypeException("Type mismatch");
        }
        RefValue refValue = (RefValue) expressionValue;
        try {
            int address = refValue.getAddress();
            return heap.lookUp(address);
        } catch (Exception e) {
            throw new AddressNotInHeap("Address not in heap");
        }

    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws Exception {
        Type type =  expression.typecheck(typeEnv);
        if (type instanceof RefType){
            RefType refType = (RefType) type;
            return refType.inner();
        }
        else  throw new InvalidTypeException("the RH argument is not a RefType");
    }
}
