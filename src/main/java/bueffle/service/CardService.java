package bueffle.service;

import bueffle.db.entity.Card;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CardService {

    private List<Card> cards = Arrays.asList(
            new Card("Warum ist Banana krumm?", "Weil sie nicht gerade ist"),
            new Card("Warum sind Pinguine nicht ro", "Weil sie schwarz sind"),
            new Card("Warum schnurren Katzen", "Damit sie nicht bellen müssen")
    );

    public List<Card> getAllCards() {
        return cards;
    }
}
