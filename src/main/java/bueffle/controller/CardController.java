package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    /**
     * Returns a List of all cards.
     * @return All Cards as List.
     */
    @GetMapping("/cards")
    public Page<Card> findCardsByQuestion(@RequestParam(value = "question", required = false) String question, Pageable pageable) {
        if (question == null) {
            return cardService.getAllCards();
        } else {
            return cardService.findByQuestion(question, pageable);
        }
    }

    /**
     * Gets the Card for a provided Id.
     * @param cardId the Id which should be returned.
     * @return A Card Object for an Id.
     */
    @GetMapping("/cards/{cardId}")
    public Card getCard(@PathVariable Long cardId) {
        return cardService.getCard(cardId);
    }

    /**
     * Gets the collections of a card while providing an id.
     * @param cardId The Id of the card to get.
     * @return The collections of the card with the provided Id.
     */
    @GetMapping("/cards/{cardId}/collections")
    public List<Collection> getCollectionsFromCards(@PathVariable Long cardId) {
        return cardService.getCollectionsFromCard(cardId);
    }

    /**
     * Adds a card.
     * @param card the body of the Card Object which should be created.
     */
    @PostMapping("/cards")
    public void addCard(@RequestBody Card card) {
        cardService.addCard(card);
    }

     /**
     * Adds card with {cardId} to collection with {collectionId}
     * @param cardId The Id of the card which should be added to the collection.
     * @param collectionId The Id of the collection which the card should be added to.
     */
    @PostMapping("/cards/{cardId}/collections/{collectionId}")
    public void addCardToCollection(@PathVariable Long cardId, @PathVariable Long collectionId) {
        cardService.addCardToCollection(cardId, collectionId);
    }

    /**
     * Updates a card while providing a Id.
     * @param card the updated card body
     * @param cardId the Id of the card to update.
     */
    @PutMapping("/cards/{cardId}")
    public void updateCard(@RequestBody Card card, @PathVariable Long cardId) {
        cardService.updateCard(card, cardId);
    }

    /**
     * Deletes a card.
     * @param cardId the Id of the card which should be deleted.
     */
    @DeleteMapping("/cards/{cardId}")
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

}
