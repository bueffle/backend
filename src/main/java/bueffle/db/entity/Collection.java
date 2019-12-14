package bueffle.db.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private boolean isPublic = false;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "collections")
    private Set<Card> cards = new HashSet<>();

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "collection")
    private Set<LearningRun> learningRuns;

    // For deserialization purposes, we must have a zero-arg constructor.
    public Collection() {}

    public Collection(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void emptyRestrictedFields() {
        if (owner != null) {
            owner.emptyRestrictedFields();
        }
        cards.clear();
    }

    public Set<Card> getCards() {
        return cards;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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
