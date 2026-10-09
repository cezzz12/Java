package repository.exception;

public class ProgramStateNotFound extends RuntimeException {
    public ProgramStateNotFound(String message) {
        super(message);
    }
}
