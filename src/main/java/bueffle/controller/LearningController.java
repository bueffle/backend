package bueffle.controller;

import bueffle.db.entity.Card;
import bueffle.db.entity.CardInLearningRun;
import bueffle.db.entity.LearningRun;
import bueffle.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LearningController {

    @Autowired
    private LearningService learningService;

    /**
     * POST Mapping for starting a learning run on a collection with collectionId. The Request body contains the
     * information "learningRunPlus": "boolean" if it should start a normal run or a learning run plus.
     * @param isLearningRunPlus boolean if it should start a normal run or a learning run plus
     * @param collectionId the Id of the collection with which the learning run should be started
     * @return a Learning Run
     */
    @PostMapping("/collections/{collectionId}/learn")
    public LearningRun getCardsFromCollection(@RequestBody LearningRun isLearningRunPlus, @PathVariable Long collectionId) {
        return learningService.start(isLearningRunPlus, collectionId);
    }

    /**
     * Method for calling after the learning run has been started. Just call /next and the next card in the learning
     * run with id learnId will be provided. It doesn't matter if it's a normal run or a learning run plus.
     * @param learnId the Id of the learning run
     * @return the next Card in this particular run
     */
    @GetMapping("/learn/{learnId}/next")
    public Card next(@PathVariable Long learnId) {
        return learningService.next(learnId);
    }

    /**
     * Set's the answer status in the request body "answeredCorrectly": "boolean" for the last shown card in a given
     * learning run id. The last shown card should be the one which is open right now in the frontend, so you can set
     * the answer status straight with a Put resuqest on /learn/{learnid}
     * @param answerStatus boolean status of the answer
     * @param learnId the if of the learning run
     */
    @PutMapping("/learn/{learnId}")
    public void setAnswer(@RequestBody CardInLearningRun answerStatus, @PathVariable Long learnId) {
        learningService.setAnswer(answerStatus, learnId);
    }
}

