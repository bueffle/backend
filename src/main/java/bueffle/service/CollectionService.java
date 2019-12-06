package bueffle.service;

import bueffle.db.entity.Collection;
import bueffle.exception.CollectionNotFoundException;
import bueffle.model.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    boolean defaultCollectionExists() {
        return collectionRepository.findByName("default") != null;
    }

    void addDefaultCollection() {
        addCollection(new Collection("default", "default"));
    }

    public List<Collection> getAllCollections() {
        return new ArrayList<>(collectionRepository.findAll());
    }

    public Collection getCollection(Long id) {
        return collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
    }

    public void addCollection(Collection collection) {
        collectionRepository.save(collection);
    }

    /**
     * Updates name and description of an existing collection
     * @param newColl the fields which should be updated are contained in this collection instance
     * @param collectionId the id of the collection which should be updated
     */
    public void updateCollection(Collection newColl, Long collectionId) {
        collectionRepository.findById(collectionId)
                .map(collection -> {
                    collection.setName(newColl.getName());
                    collection.setDescription(newColl.getDescription());
                    return collectionRepository.save(collection);
                })
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
    }

    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }

    public Collection findByName(String name) {
        return collectionRepository.findByName(name);
    }

    public Optional<Collection> findById(Long collectionId) {
        return collectionRepository.findById(collectionId);
    }
}
