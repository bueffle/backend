package bueffle.service;

import bueffle.db.entity.Collection;
import bueffle.exceptions.CollectionNotFoundException;
import bueffle.model.CollectionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;

    CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
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

    public void updateCollection(Collection newColl, Long id) {
        collectionRepository.findById(id)
                .map(collection -> {
                    collection.setName(newColl.getName());
                    collection.setDescription(newColl.getDescription());
                    return collectionRepository.save(collection);
                })
                .orElseGet(() -> {
                    newColl.setId(id);
                    return collectionRepository.save(newColl);
                });
    }

    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }
}
