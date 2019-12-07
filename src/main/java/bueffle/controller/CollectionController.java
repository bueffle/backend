package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    /**
     * Gets a List of all collections.
     * @return All Collections as List.
     */
    @GetMapping("/collections")
    public List<Collection> indexCollections() {
        return collectionService.getAllCollections();
    }

    /**
     * Creates a new collection.
     * @param collection The body of the new collection.
     */
    @PostMapping("/collections")
    public void addCollection(@RequestBody Collection collection) {
        collectionService.addCollection(collection);
    }

    /**
     * Gets the name of a collection while providing an id.
     * @param collectionId The Id of the collection to get.
     * @return The body of the collection with the provided Id.
     */
    @GetMapping("/collections/{collectionId}")
    public Collection getCollection(@PathVariable Long collectionId) {
        return collectionService.getCollection(collectionId);
    }

    /**
     * Gets the cards of a collection while providing an id.
     * @param collectionId The Id of the collection to get.
     * @return The cards of the collection with the provided Id.
     */
    @GetMapping("/collections/{collectionId}/cards")
    public List<Card> getCardsFromCollection(@PathVariable Long collectionId) {
        return collectionService.getCardsFromCollection(collectionId);
    }

    /**
     * Updates a collection.
     * @param collection The body of the updated collection.
     * @param collectionId The Id of the collection to update.
     */
    @PutMapping("/collections/{collectionId}")
    public void updateCollection(@RequestBody Collection collection, @PathVariable Long collectionId) {
        collectionService.updateCollection(collection, collectionId);
    }

    /**
     * Deletes a collection.
     * @param collectionId the Id of the collection to delete.
     */
    @DeleteMapping("/collections/{collectionId}")
    public void deleteCollection(@PathVariable Long collectionId) {
        collectionService.deleteCollection(collectionId);
    }

}
