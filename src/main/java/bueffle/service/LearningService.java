package bueffle.service;


import bueffle.db.entity.Card;
import bueffle.db.entity.CardInLearningRun;
import bueffle.db.entity.LearningRun;
import bueffle.exception.CollectionNotFoundException;
import bueffle.exception.LearningRunNotFoundException;
import bueffle.model.CardInLearningRunRepository;
import bueffle.model.LearningRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LearningService {

    @Autowired
    private UserService userService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private LearningRunRepository learningRunRepository;

    @Autowired
    private CardInLearningRunRepository cardInLearningRunRepository;

    public LearningRun start(Long collectionId) {
        LearningRun learningRun = new LearningRun();
        learningRun.setCollection(collectionService.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException(collectionId)));
        learningRun.setOwner(userService.findByUsername(userService.findLoggedInUsername()).orElse(null));
        LearningRun result = learningRunRepository.save(learningRun);

        Set<Card> cards = collectionService.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId)).getCards();
        cards.forEach(card -> {
            cardInLearningRunRepository.save(
                    new CardInLearningRun(learningRun, card)
            );
        });
        cards.forEach(Card::emptyRestrictedFields);
        if(result.getOwner()!= null) {result.getOwner().emptyRestrictedFields();}
        return learningRun;
    }

    public List<LearningRun> getAll() {
        return learningRunRepository.findAll();
    }

    public Card next(Long learnId) {
        LearningRun learningRun = learningRunRepository.findById(learnId)
                .orElseThrow(() -> new LearningRunNotFoundException(learnId));
        if (!learningRun.isLearningRunPlus()) {
            Set<CardInLearningRun> cardInLearningRuns = learningRun.getCardInLearningRuns();
            CardInLearningRun cardInLearningRun = cardInLearningRuns.stream().filter(c -> c.getShownCounter() < 1).findFirst().orElse(
                    new CardInLearningRun());
            cardInLearningRun.increaseShownCounter();
            cardInLearningRunRepository.save(cardInLearningRun);
            Card card = cardInLearningRun.getCard();
            if (card != null) {card.emptyRestrictedFields();}
            return card;
        }
        else {
            return null;
        }
    }

}
