package bueffle.exception;

public class LearningRunNotFoundException extends RuntimeException {
    public LearningRunNotFoundException(Long id) {
        super("Could not find learning run " + id);
    }
}
