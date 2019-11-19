package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.exception.CardNotFoundException;
import bueffle.exception.CollectionNotFoundException;
import bueffle.model.CardRepository;
import bueffle.model.CollectionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackendService {

    private final CardRepository cardRepository;
    private final CollectionRepository collectionRepository;

    BackendService(CardRepository cardRepository, CollectionRepository collectionRepository) {
        this.cardRepository = cardRepository;
        this.collectionRepository = collectionRepository;
    }

    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
    }

    public List<Card> getAllCards() {
        return new ArrayList<>(cardRepository.findAll());
    }

    /**
     * Creates new card. If there is no default collection yet, a default collection gets created.
     * The new card is added to the default collection then.
     * @param card the card to add
     */
    public void addCard(Card card) {
        if(!defaultCollectionExists()) {
            addDefaultCollection();
        }
        card.addCollection(getCollectionByName("default"));
        cardRepository.save(card);
    }

    /**
     * Updates name and description of an existing card
     * @param newCard the fields which should be updated are contained in this card instance
     * @param cardId the id of the card which should be updated
     */
    public void updateCard(Card newCard, Long cardId) {
        cardRepository.findById(cardId)
                .map(card -> {
                    card.setQuestion(newCard.getQuestion());
                    card.setAnswer(newCard.getAnswer());
                    return cardRepository.save(card);
                })
                .orElseThrow(() -> new CollectionNotFoundException(cardId));
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    private boolean defaultCollectionExists() {
        return getCollectionByName("default") != null;
    }

    private void addDefaultCollection() {
        addCollection(new Collection("default", "default"));
    }

    /**
     * Adds a card with cardId to a collection with collectionId. In both cases, the id gets checked
     * and an appropriate Exception gets thrown when the id can't be found in the database.
     * @param cardId id of the card which should be added to the collection
     * @param collectionId id of the collection to which the card should be added
     */
    public void addCardToCollection(Long cardId, Long collectionId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        card.addCollection(collectionRepository.findById(collectionId)
                                .orElseThrow(() -> new CollectionNotFoundException(collectionId)));
        cardRepository.save(card);
    }

    public List<Collection> getAllCollections() {
        return new ArrayList<>(collectionRepository.findAll());
    }

    public Collection getCollection(Long id) {
        return collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
    }

    private Collection getCollectionByName(String name) {
        return collectionRepository.findByName(name);
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

}
