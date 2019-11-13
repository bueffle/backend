package bueffle.service;

import bueffle.db.entity.Card;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CardService {

    private List<Card> cards = new ArrayList<>(Arrays.asList(
            new Card("banane","Warum ist Banana krumm?", "Weil sie nicht gerade ist"),
            new Card("pinug" ,"Warum sind Pinguine nicht ro", "Weil sie schwarz sind"),
            new Card("katze", "Warum schnurren Katzen", "Damit sie nicht bellen müssen")
    ));

    public List<Card> getAllCards() {
        return cards;
    }

    public Card getCard(String id) {
        return cards.stream().filter(t -> t.getId().equals(id)).findFirst().get();
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
