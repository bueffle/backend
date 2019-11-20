package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.service.BackendService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BackendController {

    private final BackendService backendService;

    BackendController(BackendService backendService) {
        this.backendService = backendService;
    }

    /*
     * Mappings for requests on /cards
     */

    @GetMapping("/cards")
    public List<Card> indexCards() {
        return backendService.getAllCards();
    }

    @GetMapping("/cards/{cardId}")
    public Card getCard(@PathVariable Long cardId) {
        return backendService.getCard(cardId);
    }

    @PostMapping("/cards")
    public void addCard(@RequestBody Card card) {
        backendService.addCard(card);
    }

    //Adds card with {cardId} to collection with {collectionId}
    @PostMapping("/cards/{cardId}/collections/{collectionId}")
    public void addCardToCollection(@PathVariable Long cardId, @PathVariable Long collectionId) {
        backendService.addCardToCollection(cardId, collectionId);
    }

    @PutMapping("/cards/{cardId}")
    public void updateCard(@RequestBody Card card, @PathVariable Long cardId) {
        backendService.updateCard(card, cardId);
    }

    @DeleteMapping("/cards/{cardId}")
    public void deleteCard(@PathVariable Long cardId) {
        backendService.deleteCard(cardId);
    }

    /*
     * Mappings for requests on /collections
     */

    @GetMapping("/collections")
    public List<Collection> indexCollections() {
        return backendService.getAllCollections();
    }

    @PostMapping("/collections")
    public void addCollection(@RequestBody Collection collection) {
        backendService.addCollection(collection);
    }

    @GetMapping("/collections/{collectionId}")
    public Collection getCollection(@PathVariable Long collectionId) {
        return backendService.getCollection(collectionId);
    }

    @PutMapping("/collections/{collectionId}")
    public void updateCollection(@RequestBody Collection collection, @PathVariable Long collectionId) {
        backendService.updateCollection(collection, collectionId);
    }

    @DeleteMapping("/collections/{collectionId}")
    public void deleteCollection(@PathVariable Long collectionId) {
        backendService.deleteCollection(collectionId);
    }

}