package bueffle.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class LearningRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime lastModified = LocalDateTime.now();
    private boolean isLearningRunPlus = false;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Collection collection;

    @OneToMany(mappedBy = "learningRun")
    private Set<CardInLearningRun> cardInLearningRuns = new HashSet<>();

    private Long lastAnsweredCardInLearningRunId;

    public LearningRun() {}

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

    public boolean isLearningRunPlus() {
        return isLearningRunPlus;
    }

    public void setLearningRunPlus(boolean learningRunPlus) {
        isLearningRunPlus = learningRunPlus;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<CardInLearningRun> getCardInLearningRuns() {
        return cardInLearningRuns;
    }

    public void addCardInLearningRun(CardInLearningRun cardInLearningRun) {
        cardInLearningRuns.add(cardInLearningRun);
    }

    public Long getLastAnsweredCardInLearningRunId() {
        return lastAnsweredCardInLearningRunId;
    }

    public void setLastAnsweredCardInLearningRunId(Long lastAnsweredCardInLearningRun) {
        this.lastAnsweredCardInLearningRunId = lastAnsweredCardInLearningRun;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public void emptyRestrictedFields() {
        if (hasOwner()) {
            owner.emptyRestrictedFields();
        }
    }
}
