package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/cards")
    public List<Card> index() {
        return cardService.getAllCards();
    }

    @GetMapping("/cards/{id}")
    public Card getCard(@PathVariable String id) {
        return cardService.getCard(id);
    }

    @PostMapping("/cards")
    public void addCard(@RequestBody Card card) {
        cardService.addCard(card);
    }
}