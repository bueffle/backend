package bueffle.db.entity;
import lombok.Data;

import javax.persistence.*;

@Entity
public class CardInLearningRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean answeredCorrectly = false;

    @ManyToOne
    private LearningRun learningRun;

    @ManyToOne
    private Card card;

    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }

    public long getId() {
        return id;
    }
}


