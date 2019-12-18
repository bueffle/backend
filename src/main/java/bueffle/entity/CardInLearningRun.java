package bueffle.entity;

import javax.persistence.*;

@Entity
public class CardInLearningRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean answeredCorrectly = false;
    private int shownCounter = 0;

    @ManyToOne
    private LearningRun learningRun;

    @ManyToOne
    private Card card;

    public CardInLearningRun() {}

    public CardInLearningRun(LearningRun learningRun, Card card) {
        this.learningRun = learningRun;
        this.card = card;
    }

    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }

    public long getId() {
        return id;
    }

    public LearningRun getLearningRun() {
        return learningRun;
    }

    public void setLearningRun(LearningRun learningRun) {
        this.learningRun = learningRun;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getShownCounter() {
        return shownCounter;
    }

    public void increaseShownCounter() {
        shownCounter++;
    }

    public boolean hasNotBeenShown() {
        return shownCounter == 0;
    }

    public void emptyRestrictedFields() {
        learningRun = null;
    }

    public void setId(long id) {
        this.id = id;
    }
}


