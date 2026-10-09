package state.exceptions;

public class VariableNotInTableException extends RuntimeException {
    public VariableNotInTableException(String message) {
        super(message);
    }
}
