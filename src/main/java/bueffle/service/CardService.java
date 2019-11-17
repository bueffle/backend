package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.exceptions.CardNotFoundException;
import bueffle.model.CardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        return new ArrayList<>(cardRepository.findAll());
    }

    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
    }

    public void addCard(Card card) {
       cardRepository.save(card);
    }

    public void updateCard(Card newCard, Long cardId) {
        cardRepository.findById(cardId)
                .map(card -> {
                    card.setQuestion(newCard.getQuestion());
                    card.setAnswer(newCard.getAnswer());
                    return cardRepository.save(card);
                })
                .orElseGet(() -> {
                    newCard.setId(cardId);
                    return cardRepository.save(newCard);
                });
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    public ArrayList<Card> getAllCardsForCollection(Long collectionId) {
        return new ArrayList<>(cardRepository.findByCollectionId(collectionId));
    }
}
