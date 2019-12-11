package bueffle.db.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class LearningRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime lastModified = LocalDateTime.now();

    @ManyToOne
    private User owner;

    @ManyToOne
    private Collection collection;

    @OneToMany(mappedBy = "learningRun")
    private Set<CardInLearningRun> cardInLearningRuns;

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public long getId() {
        return id;
    }
}
