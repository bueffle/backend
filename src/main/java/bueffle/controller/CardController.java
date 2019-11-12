package bueffle.controller;

import bueffle.db.entity.Card;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class CardController {

    @RequestMapping("/cards")
    public List<Card> index() {
        return Arrays.asList(
                new Card("Warum ist Banana krumm?", "Weil sie nicht gerade ist"),
                new Card("Warum sind Pinguine nicht ro", "Weil sie schwarz sind"),
                new Card("Warum schnurren Katzen", "Damit sie nicht bellen müssen")
        );
    }

}