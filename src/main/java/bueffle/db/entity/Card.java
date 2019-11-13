package bueffle.db.entity;

public class Card {

    private String id;
    private String question;
    private String answer;

    public Card(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    };

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

    public String getId() {
        return id;
    }
}
