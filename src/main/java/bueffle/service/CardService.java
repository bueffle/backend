package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.db.entity.User;
import bueffle.exception.CardNotFoundException;
import bueffle.exception.CollectionNotFoundException;
import bueffle.exception.NoAccessException;
import bueffle.exception.UserNotFoundException;
import bueffle.model.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService userService;

    /**
     * Shows one card by providing an Id.
     * @param cardId The Id to show the card for.
     * @return a Card Object.
     */
    public Card getCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        if (!hasPermissionsToAccessCard(card)) {
            throw new NoAccessException("card");
        }
        card.emptyRestrictedFields();
        return card;
    }

    /**
     * Returns all public cards as List of Cards.
     * @return List of all cards.
     */
    public Page<Card> getAllCards() {
        List<Card> cards = (cardRepository.findByisPublicTrue());
        cards.forEach(Card::emptyRestrictedFields);
        return new PageImpl<>(cards);
    }

    /**
     * Gets all the private cards
     * @return all private cards
     */
    public Page<Card> getAllOwnCards() {
        List<Card> cards = cardRepository
                .findByOwner(userService.findByUsername(userService.findLoggedInUsername())
                        .orElseThrow(UserNotFoundException::new));
        cards.forEach(Card::emptyRestrictedFields);
        return new PageImpl<>(cards);
    }

    /**
     * Shows the collections of a card. both get checked for permissions.
     * @param cardId the Id of the card who's collections should be shown.
     * @return List of collections of the card
     */
    public List<Collection> getCollectionsFromCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        if (!hasPermissionsToAccessCard(card)) {
            throw new NoAccessException("card");
        }
        List<Collection> collections =
                collectionService.onlyShowCollectionsWithPermissions(new ArrayList<>(card.getCollections()));
        collections.forEach(Collection::emptyRestrictedFields);
        return collections;
    }

    /**
     * Finds a card by providing a Question as String.
     * @param question provided String
     * @param pageable a pageable Object
     * @return Page which contains the card/-s
     */
    public Page<Card> findByQuestion(String question, Pageable pageable) {
        List<Card> cards = onlyShowCardsWithPermissions(cardRepository.findByQuestion(question, pageable));
        cards.forEach(Card::emptyRestrictedFields);
        return new PageImpl<>(cards);
    }

    /**
     * Finds a card by providing a userId.
     * @param userId id of the User
     * @param pageable a pageable Object
     * @return Page which contains the card/-s
     */
    public Page<Card> findByUserId(Long userId, Pageable pageable) {
        List<Card> cards = onlyShowCardsWithPermissions(cardRepository.findCardsByOwnerId(userId, pageable));
        cards.forEach(Card::emptyRestrictedFields);
        return new PageImpl<>(cards);
    }


    /**
     * Creates new card. The new card is added to the default collection of the user then.
     * @param card the card to add
     */
    public void add(Card card) {
        card.setOwner(userService.findByUsername(userService.findLoggedInUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Not found: " + userService.findLoggedInUsername())
        ));
        cardRepository.save(card);
    }

    /**
     * Updates name and description of an existing card
     * @param newCard the fields which should be updated are contained in this card instance
     * @param cardId the id of the card which should be updated
     */
    public void updateCard(Card newCard, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        if (!hasPermissionsToAccessCard(card)) {
            throw new NoAccessException("card");
        }
        card.setQuestion(newCard.getQuestion());
        card.setAnswer(newCard.getAnswer());
        card.setPublic(newCard.isPublic());
        cardRepository.save(card);
    }

    /**
     * Deletes on card by Id.
     * @param cardId the Id of the card which should be deleted.
     */
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        if (!hasPermissionsToAccessCard(card)) {
            throw new NoAccessException("card");
        }
        getCollectionsFromCard(cardId).forEach(Collection::emptyRestrictedFields);
        cardRepository.deleteById(cardId);
    }

    /**
     * Adds a card with cardId to a collection with collectionId. In both cases, the id gets checked
     * and an appropriate Exception gets thrown when the id can't be found in the database.
     * @param cardId id of the card which should be added to the collection
     * @param collectionId id of the collection to which the card should be added
     */
    public void addCardToCollection(Long cardId, Long collectionId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        if (!hasPermissionsToAccessCard(card)) {
            throw new NoAccessException("card");
        }
        Collection collection = collectionService.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));
        if (!collectionService.hasPermissionsToAccessCollection(collection)) {
            throw new NoAccessException("collection");
        }
        card.addCollection(collection);
        cardRepository.save(card);
    }

    /**
     * Checks the permissions to a given card.
     * @param card the card to check the permissions on.
     */
    boolean hasPermissionsToAccessCard(Card card) {
        boolean hasPermissionsToAccess = false;
        if(card.isPublic()) {
            hasPermissionsToAccess = true;
        }
        else {
            User user = userService.findByUsername(userService.findLoggedInUsername())
                    .orElse(new User("Anonymous"));
            if (card.getOwner().equals(user)) {
                hasPermissionsToAccess = true;
            }
        }
        return hasPermissionsToAccess;
    }

    /**
     * Checks the permissions on every element of a List and filters the elements out which are either
     * not public and the user is not the owner.
     * @param cards the List of objects to check the permissions on.
     * @return the filtered List which contains just the objects with permissions on.
     */
    List<Card> onlyShowCardsWithPermissions(List<Card> cards) {
        User user = userService.findByUsername(userService.findLoggedInUsername())
                .orElse(new User("Anonymous"));
        return cards.stream()
                .filter(card -> card.isPublic() || card.getOwner().equals(user))
                .collect(Collectors.toList());
    }


}
