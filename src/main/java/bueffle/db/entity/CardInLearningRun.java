package bueffle.db.entity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CardInLearningRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private LearningRun learningRun;

    @ManyToOne
    private Card card;

}


