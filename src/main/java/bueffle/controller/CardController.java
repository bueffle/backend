package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {

    private final CardService cardService;

    CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards")
    public List<Card> index() {
        return cardService.getAllCards();
    }

    @PostMapping("/cards")
    public void addCard(@RequestBody Card card) {
        cardService.addCard(card);
    }

    @GetMapping("/cards/{id}")
    public Card getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @PutMapping("/cards/{id}")
    public void updateCard(@RequestBody Card card, @PathVariable Long id) {
        cardService.updateCard(card, id);
    }

    @DeleteMapping("/cards/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}