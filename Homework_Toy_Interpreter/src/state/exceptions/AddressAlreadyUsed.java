package state.exceptions;

public class AddressAlreadyUsed extends RuntimeException {
    public AddressAlreadyUsed(String message) {
        super(message);
    }
}
