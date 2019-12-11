package bueffle.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class LearningRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Collection collection;

    @OneToMany(mappedBy = "learningRun")
    private Set<CardInLearningRun> cardInLearningRuns;

}
