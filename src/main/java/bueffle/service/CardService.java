package bueffle.service;

import bueffle.db.entity.Card;
import bueffle.model.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        cardRepository.findAll().forEach(cards::add);
        return cards;
    }

    public Card getCard(Long id) {
        return cardRepository.findById(id).get();
    }

    public void addCard(Card card) {
       cardRepository.save(card);
    }

    public void updateCard(Card card, Long id) {
        cardRepository.save(card);
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}
