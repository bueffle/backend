package bueffle.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CardTest {

    int CARDS = 5;
    List<Card> testCards = new ArrayList<>();

    @Before
    public void before() {
        for (int i = 0; i < CARDS; i++) {
            Card card = new Card("TestQuestion " + i, "TestAnswer " + i);
            card.setOwner(new User("User " + i, "password" + i, "password" + i));
            card.addCollection(new Collection("Collection " + i, "Description " + i));
            testCards.add(card);
        }
    }

    @Test
    public void add() {
        for (int i = 0; i < testCards.size(); i++) {
            Assertions.assertEquals("TestQuestion " + i, testCards.get(i).getQuestion());
            Assertions.assertEquals("TestAnswer " + i, testCards.get(i).getAnswer());
            Assertions.assertFalse(testCards.get(i).isPublic());
        }
    }

    @Test
    public void update() {
        testCards.forEach(card -> {
            String newUsername = "User XXX";
            card.setOwner(new User(newUsername));
            Assertions.assertEquals(newUsername, card.getOwner().getUsername());

            Collection collection = new Collection("New Coll", "New Desc");
            card.addCollection(collection);
            Assertions.assertTrue(card.getCollections().contains(collection));
        });
    }

    @Test
    public void emptyProblematicFields() {
        testCards.forEach(card -> {
            card.emptyRestrictedFields();
            Assertions.assertTrue(card.getCollections().isEmpty());
            Assertions.assertNull(card.getOwner().getPassword());
        });
    }

}
