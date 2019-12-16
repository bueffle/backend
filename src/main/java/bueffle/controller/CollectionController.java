package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    /**
     * Returns a List of all public collections if used with no parameters.
     * if used with ?user=userId for example /collections?user=1 it will only return the collections owned by user 1
     * if used with ?name=name for example /collections?name=hi it will only return the collections with the name "hi"
     * @return All collections as List.
     */
    @GetMapping("/collections")
    public Page<Collection> getCollections(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "user", required = false) Long userId,
            Pageable pageable) {
        if (name == null && userId == null) {
            return collectionService.getAllCollections();
        }
        else if (name != null){
            return collectionService.findByName(name, pageable);
        }
        else {
            return collectionService.findByUserId(userId, pageable);
        }
    }

    /**
     * Returns a List of all owned collections.
     * @return All owned collections as List.
     */
    @GetMapping("/collections/own")
    public Page<Collection> getOwnCollections(Pageable pageable) {
        return collectionService.getAllOwnCollections();
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

    @PostMapping("/collections")
    public void addCollection(@RequestBody Collection collection) {
        collectionService.addCollection(collection);
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
