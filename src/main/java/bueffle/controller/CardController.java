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

    @GetMapping("/cards/{cardId}")
    public Card getCard(@PathVariable Long cardId) {
        return cardService.getCard(cardId);
    }

    @GetMapping("/collections/{collectionId}/cards")
    public List<Card> getAllCardsForCollection(@PathVariable Long collectionId) {
        return cardService.getAllCardsForCollection(collectionId);
    }

    @PutMapping("/cards/{cardId}")
    public void updateCard(@RequestBody Card card, @PathVariable Long cardId) {
        cardService.updateCard(card, cardId);
    }

    @DeleteMapping("/cards/{cardId}")
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }
}