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

    public void addCard(Card card) {
        addCardToDefaultCollection(card);
    }

    public void updateCard(Card newCard, Long cardId) {
        cardRepository.findById(cardId)
                .map(card -> {
                    card.setQuestion(newCard.getQuestion());
                    card.setAnswer(newCard.getAnswer());
                    return cardRepository.save(card);
                });
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    private void addCardToDefaultCollection(Card card) {
        if(!defaultCollectionExists()) {
            addDefaultCollection();
        }
        card.addCollection(getCollectionByName("default"));
        cardRepository.save(card);
    }

    private boolean defaultCollectionExists() {
        return getCollectionByName("default") != null;
    }

    private void addDefaultCollection() {
        addCollection(new Collection("default", "default"));
    }

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

    public void updateCollection(Collection newColl, Long id) {
        collectionRepository.findById(id)
                .map(collection -> {
                    collection.setName(newColl.getName());
                    collection.setDescription(newColl.getDescription());
                    return collectionRepository.save(collection);
                });
    }

    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }

}
