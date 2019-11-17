package bueffle.controller;

import bueffle.db.entity.Collection;
import bueffle.service.CollectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CollectionController {

    private final CollectionService collectionService;

    CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping("/collections")
    public List<Collection> index() {
        return collectionService.getAllCollections();
    }

    @PostMapping("/collections")
    public void addCollection(@RequestBody Collection collection) {
        collectionService.addCollection(collection);
    }

    @GetMapping("/collections/{collectionId}")
    public Collection getCollection(@PathVariable Long collectionId) {
        return collectionService.getCollection(collectionId);
    }

    @PutMapping("/collections/{collectionId}")
    public void updateCollection(@RequestBody Collection collection, @PathVariable Long collectionId) {
        collectionService.updateCollection(collection, collectionId);
    }

    @DeleteMapping("/collections/{collectionId}")
    public void deleteCollection(@PathVariable Long collectionId) {
        collectionService.deleteCollection(collectionId);
    }
}