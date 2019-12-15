package bueffle.exception;

public class CardInLearningRunNotFoundException extends RuntimeException {
    public CardInLearningRunNotFoundException(Long id) {
        super("Could not find card learning run " + id);
    }
}
