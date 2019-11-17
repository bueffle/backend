package bueffle.exceptions;

public class CollectionNotFoundException extends RuntimeException {
    public CollectionNotFoundException(Long id) {
        super("Could not find collection " + id);
    }
}
