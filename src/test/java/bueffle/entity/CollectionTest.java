package bueffle.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CollectionTest {

    int COLLECTIONS = 5;
    List<Collection> testCollections = new ArrayList<>();

    @Before
    public void before() {
        for (int i = 0; i < COLLECTIONS; i++) {
            Collection collection = new Collection("TestName " + i, "TestDescription " + i);
            collection.setOwner(new User("User " + i, "password" + i, "password" + i));

            Card card = new Card("TestQuestion " + i, "TestAnswer " + i);
            card.addCollection(collection);
            testCollections.add(collection);
        }
    }

    @Test
    public void add() {
        for (int i = 0; i < testCollections.size(); i++) {
            Assertions.assertEquals("TestName " + i, testCollections.get(i).getName());
            Assertions.assertEquals("TestDescription " + i, testCollections.get(i).getDescription());
            Assertions.assertFalse(testCollections.get(i).isPublic());
        }
    }

    @Test
    public void update() {
        testCollections.forEach(collection -> {
            String newUsername = "User XXX";
            collection.setOwner(new User(newUsername));
            Assertions.assertEquals(newUsername, collection.getOwner().getUsername());

            Card card = new Card("New Question", "New Answer");
            card.addCollection(collection);
            Assertions.assertTrue(card.getCollections().contains(collection));
        });
    }

    @Test
    public void emptyProblematicFields() {
        testCollections.forEach(collection -> {
            collection.emptyRestrictedFields();
            Assertions.assertTrue(collection.getCards().isEmpty());
            Assertions.assertNull(collection.getOwner().getPassword());
        });
    }

}
