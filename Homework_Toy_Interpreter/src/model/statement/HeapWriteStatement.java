package model.statement;

import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.types.RefType;
import model.types.Type;
import model.value.RefValue;
import model.value.Value;
import state.ProgramState;
import state.exceptions.AddressNotInHeap;
import model.expression.Expression;

import java.util.Map;
import java.util.Objects;

public record HeapWriteStatement(String var_name, Expression expression)  implements Statement{

    @Override
    public ProgramState execute(ProgramState state) {
        if (!state.symbolTable().isDefined(var_name)) {
            throw new VariableNotDefinedException();
        }
        if (!(state.symbolTable().LookUp(var_name).getType() instanceof RefType)){
            throw new InvalidTypeException("Type mismatch");
        }
        RefValue value = (RefValue) state.symbolTable().LookUp(var_name);
        int address = value.getAddress();
        Type type = value.getLocationType();
        try{
            Value val_heap = state.heap().lookUp(address);
        } catch (Exception e) {
            throw new AddressNotInHeap("Address not in heap");
        }
        Value val_expression = expression.evaluate(state.symbolTable(), state.heap());
        //IO.println(type + " " + val_expression.getType());
        if (!type.equals(val_expression.getType())){
            throw new InvalidTypeException("Type mismatch");
        }
        state.heap().update(address, val_expression);
        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        Type typevar = typeEnv.get(var_name);
        Type typexp = expression.typecheck(typeEnv);


        if (typevar instanceof RefType) {
            RefType refType = (RefType) typevar;
            if (refType.inner().equals(typexp)) {
                return typeEnv;
            } else {
                throw new InvalidTypeException("Heap write: The expression type (" + typexp +
                        ") does not match the reference's inner type (" + refType.inner() + ")");
            }
        } else {
            throw new InvalidTypeException("Heap write: Variable '" + var_name + "' is not a Reference Type");
        }
    }
    @Override
    public String toString() {
        return "HeapWrite("+var_name+","+expression.toString()+");";
    }
}
