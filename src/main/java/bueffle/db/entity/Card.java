package bueffle.db.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String question;
    private String answer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Collection> collections = new ArrayList<>();

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

    public void emptyCollections() {
        collections.clear();
    }

    public List<Collection> getCollections() {
        return collections;
    }

}
