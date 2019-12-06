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
    public List<Card> indexCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/cards/{cardId}")
    public Card getCard(@PathVariable Long cardId) {
        return cardService.getCard(cardId);
    }

    @PostMapping("/cards")
    public void addCard(@RequestBody Card card) {
        cardService.addCard(card);
    }

    //Adds card with {cardId} to collection with {collectionId}
    @PostMapping("/cards/{cardId}/collections/{collectionId}")
    public void addCardToCollection(@PathVariable Long cardId, @PathVariable Long collectionId) {
        cardService.addCardToCollection(cardId, collectionId);
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
