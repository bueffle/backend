package bueffle.service;


import bueffle.db.entity.Card;
import bueffle.db.entity.CardInLearningRun;
import bueffle.db.entity.LearningRun;
import bueffle.exception.CardInLearningRunNotFoundException;
import bueffle.exception.CollectionNotFoundException;
import bueffle.exception.LearningRunNotFoundException;
import bueffle.model.CardInLearningRunRepository;
import bueffle.model.LearningRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public LearningRun start(LearningRun learningRun, Long collectionId) {
        learningRun.setCollection(collectionService.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException(collectionId)));
        learningRun.setOwner(userService.findByUsername(userService.findLoggedInUsername()).orElse(null));
        LearningRun resultingLearningRun = learningRunRepository.save(learningRun);

        Set<Card> cards = collectionService.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId)).getCards();
        cards.forEach(card -> {
            cardInLearningRunRepository.save(
                    new CardInLearningRun(learningRun, card)
            );
        });
        cards.forEach(Card::emptyRestrictedFields);
        if(resultingLearningRun.getOwner()!= null) {resultingLearningRun.getOwner().emptyRestrictedFields();}
        return learningRun;
    }

    public List<LearningRun> getAll() {
        return learningRunRepository.findAll();
    }

    public Card next(Long learnId) {
        LearningRun learningRun = learningRunRepository.findById(learnId)
                .orElseThrow(() -> new LearningRunNotFoundException(learnId));
        Set<CardInLearningRun> cardInLearningRuns = learningRun.getCardInLearningRuns();
        CardInLearningRun cardInLearningRun;
        if (learningRun.isLearningRunPlus()) {
            cardInLearningRun = cardInLearningRuns.stream().filter(c -> !c.isAnsweredCorrectly()).findFirst().orElse(
                    new CardInLearningRun());
        }
        else {
            cardInLearningRun = cardInLearningRuns.stream().filter(c -> c.getShownCounter() < 1).findFirst().orElse(
                    new CardInLearningRun());
        }
        learningRun.setLastAnsweredCardInLearningRunId(cardInLearningRun.getId());
        cardInLearningRun.increaseShownCounter();
        cardInLearningRunRepository.save(cardInLearningRun);
        Card card = cardInLearningRun.getCard();
        if (card != null) {card.emptyRestrictedFields();}
        return card;
    }

    public void setAnswer(CardInLearningRun answerStatus, Long learnId) {
        LearningRun learningRun = learningRunRepository.findById(learnId)
                .orElseThrow(() -> new LearningRunNotFoundException(learnId));
        CardInLearningRun cardInLearningRun =
                cardInLearningRunRepository.findById(learningRun.getLastAnsweredCardInLearningRunId())
                        .orElseThrow(
                                () -> new CardInLearningRunNotFoundException(learningRun.getLastAnsweredCardInLearningRunId()));
        cardInLearningRun.setAnsweredCorrectly(answerStatus.isAnsweredCorrectly());
        cardInLearningRunRepository.save(cardInLearningRun);
    }
}
