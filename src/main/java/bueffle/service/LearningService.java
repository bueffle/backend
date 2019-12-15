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

    /**
     * Starts a learning run and sets the given collection and the starting user as owner on it and saves it.
     * It then gets all the cards and makes the relation between the learning run and the cards. The connection is made
     * by using a ManyToOne relationship to a entity called CardInLearningRun, which stores information about the card
     * in this learning run. Of course, this entity CardInLearningRun then has a OneToMany with the card entity.
     * Lastly, fields which would be problematic for the frontend or should not be shown are beeing wiped out and then
     * the resulting learning run gets returned.
     * @param learningRun a dummy object which just contains information about the run if it is a normal or plus run
     * @param collectionId the id of the collection to start the run with
     * @return the learning run object ready for output with json (with emptied problematic or restricted fields)
     */
    public LearningRun start(LearningRun learningRun, Long collectionId) {
        learningRun.setCollection(collectionService.findById(collectionId).orElseThrow(
                () -> new CollectionNotFoundException(collectionId)));
        learningRun.setOwner(userService.findByUsername(userService.findLoggedInUsername()).orElse(null));
        LearningRun resultingLearningRun = learningRunRepository.save(learningRun);

        Set<Card> cards = collectionService.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId)).getCards();
        cards.forEach(card -> {cardInLearningRunRepository.save(new CardInLearningRun(learningRun, card));});
        cards.forEach(Card::emptyRestrictedFields);
        if(resultingLearningRun.getOwner()!= null) {resultingLearningRun.getOwner().emptyRestrictedFields();}
        return resultingLearningRun;
    }

    /**
     * Gets the next card of a learning run. First, it finds the learningRun by the provided it and it's related
     * CardInLearningRuns. Gets the appropriate CardInLearningRun from it's helper method.
     * Then it increases the shown counter and saves the CardInLearningRun.
     * Lastly, it gets the associated card and wipes problematic or restricted fields an returns it.
     * @param learnId the id of the learning run
     * @return the next card object in this particular run
     */
    public Card next(Long learnId) {
        CardInLearningRun cardInLearningRun = getCardInLearningRun(learningRunRepository.findById(learnId)
                .orElseThrow(() -> new LearningRunNotFoundException(learnId)));

        cardInLearningRun.increaseShownCounter();
        cardInLearningRunRepository.save(cardInLearningRun);

        Card card = cardInLearningRun.getCard();
        if (card != null) {card.emptyRestrictedFields();}
        return card;
    }

    /**
     * Checks if it's running a normal run or a learning run plus. If it's a plus run, it searches the
     * CardsInLearningRuns for the first card with it's answer status set to incorrect. If it's a normal
     * run, it just searches for the first card which has not been shown yet.
     * @param learningRun the learningRun to get the cardInLearningRun for
     * @return the resulting CardInLearningRun
     */
    private CardInLearningRun getCardInLearningRun(LearningRun learningRun) {
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
        return cardInLearningRun;
    }

    /**
     * Sets the answer status of the last shown CardInLearningRun of a learning run.
     * @param answerStatus boolean status of the answer
     * @param learnId the id of the learning run to set
     */
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
