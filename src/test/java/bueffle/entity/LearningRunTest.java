package bueffle.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class LearningRunTest {

    int LEARNINGRUNS = 5;
    int CARDS = 10;

    List<LearningRun> testLearningRuns = new ArrayList<>();
    User user = new User("User Test", "password", "password");

    @BeforeEach
    public void before() {
        for (int i = 0; i < LEARNINGRUNS; i++) {
            LearningRun learningRun = new LearningRun();
            Collection collection = new Collection("Collection " + i, "Description " + i);

            for (int j = 0; j < CARDS; j++) {

                CardInLearningRun cardInLearningRun = new CardInLearningRun();
                cardInLearningRun.setId(Integer.parseInt(i + "" + j));
                learningRun.addCardInLearningRun(cardInLearningRun);

                Card card = new Card("TestQuestion " + i + "" + j, "TestAnswer " + i + "" + j);
                card.setOwner(user);
                card.addCollection(collection);
                cardInLearningRun.setCard(card);
            }

            learningRun.setCollection(collection);
            learningRun.setOwner(user);
            testLearningRuns.add(learningRun);
        }
    }

    @Test
    public void start() {
        for (int i = 0; i < testLearningRuns.size(); i++) {

            testLearningRuns.get(i).getCardInLearningRuns().forEach(cardInLearningRun -> {
                String question = cardInLearningRun.getCard().getQuestion();
                Assertions.assertEquals("TestQuestion", question.substring(0, question.indexOf(' ')));

                Assertions.assertEquals(user, cardInLearningRun.getCard().getOwner());

                cardInLearningRun.getCard().emptyRestrictedFields();
                Assertions.assertNull(cardInLearningRun.getCard().getOwner().getPassword());
            });

            testLearningRuns.forEach(testLearningRun -> {
                Assertions.assertEquals(user, testLearningRun.getOwner());
            });

        }
    }

    @Test
    public void learningRunPlus() {
        for (int i = 0; i < testLearningRuns.size(); i++) {

            //Tests a learning run plus. Runs as long as all questions are set to answered correctly.
            testLearningRuns.forEach(testLearningRun -> {
                Set<CardInLearningRun> cardInLearningRuns = testLearningRun.getCardInLearningRuns();

                //assert that all answers are wrong at the start
                Assertions.assertTrue(cardInLearningRuns.stream().noneMatch(CardInLearningRun::isAnsweredCorrectly));

                while (!cardInLearningRuns.stream().allMatch(CardInLearningRun::isAnsweredCorrectly)) {
                    cardInLearningRuns.stream()
                            .filter(cardInLearningRun -> !cardInLearningRun.isAnsweredCorrectly())
                            .findFirst().get().setAnsweredCorrectly(true);
                }

                //asserts all the answers were set to correct when the run ends
                Assertions.assertTrue(cardInLearningRuns.stream().allMatch(CardInLearningRun::isAnsweredCorrectly));
            });
        }
    }


    @Test
    public void learningRun() {
        for (int i = 0; i < testLearningRuns.size(); i++) {

            //Tests a learning run. Runs as long as all questions have been shown once.
            testLearningRuns.forEach(testLearningRun -> {
                Set<CardInLearningRun> cardInLearningRuns = testLearningRun.getCardInLearningRuns();

                //assert that no question has been shown yet
                Assertions.assertTrue(cardInLearningRuns.stream().allMatch(CardInLearningRun::hasNotBeenShown));

                while (!cardInLearningRuns.stream().allMatch(CardInLearningRun::hasNotBeenShown)) {
                    cardInLearningRuns.stream()
                            .filter(cardInLearningRun -> !cardInLearningRun.isAnsweredCorrectly())
                            .findFirst().get().increaseShownCounter();
                }

                //asserts all questions have been shown once
                Assertions.assertTrue(cardInLearningRuns.stream().noneMatch(CardInLearningRun::hasNotBeenShown));
            });
        }
    }


}
