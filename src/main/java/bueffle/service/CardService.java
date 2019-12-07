package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.db.entity.Collection;
import bueffle.exception.CardNotFoundException;
import bueffle.exception.CollectionNotFoundException;
import bueffle.model.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CollectionService collectionService;

    /**
     * Shows one card by providing an Id.
     * @param cardId The Id to show the card for.
     * @return a Card Object.
     */
    public Card getCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        card.emptyCollections();
        return card;
    }

    /**
     * Shows the collections of a card.
     * @param cardId the Id of the card who's collections should be shown.
     * @return List of collections of the card
     */
    public List<Collection> getCollectionsFromCard(Long cardId) {
        List<Collection> collections = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId)).getCollections();
        collections.forEach(Collection::emptyCards);
        return collections;
    }

    /**
     * Returns all cards as List of Cards.
     * @return List of all cards.
     */
    public List<Card> getAllCards() {
        return new ArrayList<>(cardRepository.findAll());
    }

    /**
     * Creates new card. The new card is added to the default collection of the user then.
     * @param card the card to add
     */
    public void addCard(Card card) {
        collectionService.addCardToUserDefaultCollection(card);
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

    /**
     * Deletes on card by Id.
     * @param cardId the Id of the card which should be deleted.
     */
    public void deleteCard(Long cardId) {
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
        card.addCollection(collectionService.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId)));
        cardRepository.save(card);
    }

}
