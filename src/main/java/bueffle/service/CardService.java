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

    public Card getCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    public void addCard(Card card) {
       cardRepository.save(card);
    }

    public void updateCard(Card newCard, Long id) {
        cardRepository.findById(id)
                .map(card -> {
                    card.setQuestion(newCard.getQuestion());
                    card.setAnswer(newCard.getAnswer());
                    return cardRepository.save(card);
                })
                .orElseGet(() -> {
                    newCard.setId(id);
                    return cardRepository.save(newCard);
                });
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}
