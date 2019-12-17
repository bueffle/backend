package bueffle.db.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String question;
    private String answer;
    private boolean isPublic = false;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Collection> collections = new HashSet<>();

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "card")
    private Set<CardInLearningRun> cardInLearningRuns;

    // For deserialization purposes, we must have a zero-arg constructor.
    public Card() {}

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void addCollection(Collection collection) {
        collections.add(collection);
    }

    public void emptyRestrictedFields() {
        if (owner != null) {
            owner.emptyRestrictedFields();
        }
        collections.clear();
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void publish() {
        isPublic = true;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }
}
