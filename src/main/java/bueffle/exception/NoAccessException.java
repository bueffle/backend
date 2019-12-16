package bueffle.exception;

public class NoAccessException extends RuntimeException {
    public NoAccessException(String entity) {super("No access to this " + entity);    }
}
