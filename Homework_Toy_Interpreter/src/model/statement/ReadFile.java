package model.statement;

import model.types.Type;
import model.exception.InvalidFileExpression;
import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.expression.Expression;
import model.value.IntegerValue;
import model.value.Value;
import state.ProgramState;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public record ReadFile(Expression expression, String var_name) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value filename = expression.evaluate(state.symbolTable(), state.heap());
        if (filename.getType() != Type.STRING) {
            throw new InvalidTypeException("Cannot read file because type is not a string");
        }
        if (state.symbolTable().LookUp(var_name) == null) {
            throw new VariableNotDefinedException();
        }
        var variable = state.symbolTable().LookUp(var_name);
        if (variable.getType() != Type.INTEGER) {
            throw new InvalidTypeException("Variable " + var_name + " is not a number");
        }
        
        Value filenameValue = expression.evaluate(state.symbolTable(), state.heap());
        BufferedReader fileReader = state.fileTable().lookUp(filenameValue);
        if (fileReader == null){
            throw new InvalidFileExpression("File is not opened: " + filenameValue);
        }
        
        try {
            String line = fileReader.readLine();
            IntegerValue newValue;
            if (line == null || line.trim().isEmpty()){
                newValue = new IntegerValue(0);
            }
            else{
                newValue = new IntegerValue(Integer.parseInt(line.trim()));
            }
            state.symbolTable().updateValue(var_name, newValue);
        }
        catch (IOException e) {
            throw new InvalidFileExpression("IO Error reading file: " + e.getMessage());
        }
        catch (NumberFormatException e) {
            throw new InvalidFileExpression("Invalid number format in file: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Map<String, Type> typeCheck(Map<String, Type> typeEnv) throws Exception {
        Type vartype =  typeEnv.get(var_name);
        Type exprtype = expression.typecheck(typeEnv);
        if (vartype.equals(exprtype)) {
            return typeEnv;
        }
        else throw new InvalidTypeException("Cannot read file because type is not a string");
    }

    @Override
    public String toString() {
        return var_name + " = readFile(" + expression.toString() +");";
    }
}
