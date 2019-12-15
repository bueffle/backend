package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.exception.CollectionNotFoundException;
import bueffle.model.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserService userService;

    /**
     * Creates new collection.
     * @param collection the collection to add
     */
    public void addCollection(Collection collection) {
        collection.setOwner(userService.findByUsername(userService.findLoggedInUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Not found: " + userService.findLoggedInUsername())
        ));
        collectionRepository.save(collection);
    }

    /**
     * Gets all the collections
     * @return all collections
     */
    public Page<Collection> getAllCollections() {
        List<Collection> collections = (collectionRepository.findAll());
        collections.forEach(Collection::emptyRestrictedFields);
        return new PageImpl<>(collections);
    }

    /**
     * Shows one collection by providing an Id.
     * @param collectionId The Id to show the collection for.
     * @return a collection Object.
     */
    public Collection getCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
        collection.emptyRestrictedFields();
        return collection;
    }

    /**
     * Shows the cards of a collection.
     * @param collectionId the Id of the collection who's cards should be shown.
     * @return List of cards of the collection
     */
    public Set<Card> getCardsFromCollection(Long collectionId) {
        Set<Card> cards = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId)).getCards();
        cards.forEach(Card::emptyRestrictedFields);
        return cards;
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
                    collection.setPublic(newColl.isPublic());
                    return collectionRepository.save(collection);
                })
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
    }

    /**
     * Deletes a collection
     * @param id for deletion
     */
    public void deleteCollection(Long id) {
        getCardsFromCollection(id).forEach(Card::emptyRestrictedFields);
        collectionRepository.deleteById(id);
    }

    /**
     * Finds a collection by providing a name
     * @param name to find
     * @return collection
     */
    public Page<Collection> findByName(String name, Pageable pageable) {
        List<Collection> collections = (collectionRepository.findByName(name, pageable));
        collections.forEach(Collection::emptyRestrictedFields);
        return new PageImpl<>(collections);
    }

    /**
     * Finds a collection by providing a userId.
     * @param userId id of the User
     * @param pageable a pageable Object
     * @return Page which contains the card/-s
     */
    public Page<Collection> findByUserId(Long userId, Pageable pageable) {
        List<Collection> collections = (collectionRepository.findCollectionsByOwnerId(userId, pageable));
        collections.forEach(Collection::emptyRestrictedFields);
        return new PageImpl<>(collections);
    }

    /**
     * Finds a collection by Id
     * @param collectionId the Id to find
     * @return an Optional found collection
     */
    public Optional<Collection> findById(Long collectionId) {
        return collectionRepository.findById(collectionId);
    }

}
