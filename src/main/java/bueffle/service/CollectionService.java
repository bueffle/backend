package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.db.entity.User;
import bueffle.exception.CollectionNotFoundException;
import bueffle.exception.NoAccessException;
import bueffle.exception.UserNotFoundException;
import bueffle.model.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CardService cardService;

    /**
     * Shows one collection by providing an Id.
     * @param collectionId The Id to show the collection for.
     * @return a collection Object.
     */
    public Collection getCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
        if (!hasPermissionsToAccessCollection(collection)) {
            throw new NoAccessException("collection");
        }
        collection.emptyRestrictedFields();
        return collection;
    }

    /**
     * Gets all the public collections
     * @return all collections
     */
    public Page<Collection> getAllCollections() {
        List<Collection> collections = collectionRepository.findByisPublicTrue();
        collections.forEach(Collection::emptyRestrictedFields);
        return new PageImpl<>(collections);
    }

    /**
     * Gets all the private collections
     * @return all private collections
     */
    public Page<Collection> getAllOwnCollections() {
        List<Collection> collections = collectionRepository
                .findByOwner(userService.findByUsername(userService.findLoggedInUsername())
                .orElseThrow(UserNotFoundException::new));
        collections.forEach(Collection::emptyRestrictedFields);
        return new PageImpl<>(collections);
    }

    /**
     * Shows the cards of a collection. both get checked for permissions.
     * @param collectionId the Id of the collection who's cards should be shown.
     * @return List of cards of the collection
     */
    public List<Card> getCardsFromCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
        List<Card> cards = cardService.onlyShowCardsWithPermissions(new ArrayList<>(collection.getCards()));
        if (!hasPermissionsToAccessCollection(collection)) {
            throw new NoAccessException("collection");
        }
        cards.forEach(Card::emptyRestrictedFields);
        return cards;
    }

    /**
     * Finds a collection by providing a name
     * @param name to find
     * @return collection
     */
    public Page<Collection> findByName(String name, Pageable pageable) {
        List<Collection> collections = onlyShowCollectionsWithPermissions(collectionRepository.findByName(name, pageable));
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
        List<Collection> collections = onlyShowCollectionsWithPermissions(collectionRepository.findCollectionsByOwnerId(userId, pageable));
        collections.forEach(Collection::emptyRestrictedFields);
        return new PageImpl<>(collections);
    }

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
     * Updates name and description of an existing collection and isPublic. If it gets published, also all cards
     * contained in the collection getting published.
     * @param newColl the fields which should be updated are contained in this collection instance
     * @param collectionId the id of the collection which should be updated
     */
    public void updateCollection(Collection newColl, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
        if (!hasPermissionsToAccessCollection(collection)) {
            throw new NoAccessException("collection");
        }
        collection.setName(newColl.getName());
        collection.setDescription(newColl.getDescription());
        collection.setPublic(newColl.isPublic());
        if (newColl.isPublic()) {
            collection.getCards().forEach(Card::publish);
        }
        collectionRepository.save(collection);
    }

    /**
     * Deletes a collection
     * @param collectionId for deletion
     */
    public void deleteCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
        if (!hasPermissionsToAccessCollection(collection)) {
            throw new NoAccessException("collection");
        }
        getCardsFromCollection(collectionId).forEach(Card::emptyRestrictedFields);
        collectionRepository.deleteById(collectionId);
    }

    /**
     * Finds a collection by Id, for backend use.
     * @param collectionId the Id to find
     * @return an Optional found collection
     */
    public Optional<Collection> findById(Long collectionId) {
        return collectionRepository.findById(collectionId);
    }

    /**
     * Checks the permissions to a given collection.
     * @param collection the collection to check the permissions on.
     */
    boolean hasPermissionsToAccessCollection(Collection collection) {
        boolean hasPermissionsToAccess = false;
        if(collection.isPublic()) {
            hasPermissionsToAccess = true;
        }
        else {
            User user = userService.findByUsername(userService.findLoggedInUsername())
                    .orElse(new User("Anonymous"));
            if (collection.getOwner().equals(user)) {
                hasPermissionsToAccess = true;
            }
        }
        return hasPermissionsToAccess;
    }

    /**
     * Checks the permissions on every element of a List and filters the elements out which are either
     * not public and the user is not the owner.
     * @param collections the List of objects to check the permissions on.
     * @return the filtered List which contains just the objects with permissions on.
     */
    List<Collection> onlyShowCollectionsWithPermissions(List<Collection> collections) {
        User user = userService.findByUsername(userService.findLoggedInUsername())
                .orElse(new User("Anonymous"));
        return collections.stream()
                .filter(collection -> collection.isPublic() || collection.getOwner().equals(user))
                .collect(Collectors.toList());
    }

}
