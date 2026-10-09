package model.statement;

import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.types.RefType;
import model.types.Type;
import model.value.RefValue;
import model.value.Value;
import state.ProgramState;
import model.expression.Expression;

import java.util.Map;

public record HeapAllocStatement(String var_name, Expression expression)  implements Statement{
    @Override
    public ProgramState execute(ProgramState state) {
        if (state.symbolTable().isDefined(var_name)) {
            Value val = expression.evaluate(state.symbolTable(), state.heap());
            Value table_val = state.symbolTable().LookUp(var_name);
            RefValue ref_val = (RefValue) table_val;
            //IO.println(val.getType() + " " + ref_val.getLocationType());
            if (!val.getType().equals(ref_val.getLocationType())) {
                throw new InvalidTypeException("Type mismatch");
            }
            int addr = state.heap().add(val);
            state.symbolTable().updateValue(var_name, new RefValue(addr, val.getType()));
        }
        else{
            throw new VariableNotDefinedException();
        }
        return null;

    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        Type typevar = typeEnv.get(var_name);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp))){
            return typeEnv;
        }
        else{
            throw new InvalidTypeException("Alloc Statement: Right hand side and left have different types");
        }
    }

    @Override
    public String toString() {
        return "HeapAlloc(" + var_name + ", " + expression.toString() + ");";
    }
}
