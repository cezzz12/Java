package state.exceptions;

public class AddressNotInHeap extends RuntimeException {
    public AddressNotInHeap(String message) {
        super(message);
    }
}
