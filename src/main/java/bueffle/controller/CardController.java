package bueffle.controller;

import bueffle.entity.Card;
import bueffle.entity.Collection;
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
     * Returns a List of all public cards if used with no parameters.
     * if used with ?user=userId for example /cards?user=1 it will only return the cards owned by user 1
     * if used with ?question=question for example /cards?question=why it will only return the cards that contain "why"
     * @return All Cards as List.
     */
    @GetMapping("/cards")
    public Page<Card> getCards(
            @RequestParam(value = "question", required = false) String question,
            @RequestParam(value = "user", required = false) Long userId,
            Pageable pageable) {
        if (question == null && userId == null) {
            return cardService.getAllCards();
        }
        else if (question != null){
            return cardService.findByQuestion(question, pageable);
        }
        else {
            return cardService.findByUserId(userId, pageable);
        }
    }

    /**
     * Returns a List of all owned cards.
     * @return All owned cards as List.
     */
    @GetMapping("/cards/own")
    public Page<Card> getOwnCards(Pageable pageable) {
        return cardService.getAllOwnCards();
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
    public Card addCard(@RequestBody Card card) {
        return cardService.add(card);
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
