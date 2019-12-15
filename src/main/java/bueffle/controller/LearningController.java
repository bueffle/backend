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

    @PostMapping("/collections/{collectionId}/learn")
    public LearningRun getCardsFromCollection(@RequestBody LearningRun isLearningRunPlus, @PathVariable Long collectionId) {
        return learningService.start(isLearningRunPlus, collectionId);
    }

    @GetMapping("/learn/{learnId}/next")
    public Card next(@PathVariable Long learnId) {
        return learningService.next(learnId);
    }

    @PutMapping("/learn/{learnId}")
    public void setAnswer(@RequestBody CardInLearningRun answerStatus, @PathVariable Long learnId) {
        learningService.setAnswer(answerStatus, learnId);
    }
}

