package bueffle.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserTest {

    int USERS = 5;
    List<User> testUser = new ArrayList<>();
    Role userRole = new Role("ROLE_USER");

    @Before
    public void before() {
        for (int i = 0; i < USERS; i++) {
            User user = new User("User " + i, "password" + i, "password" + i);
            user.addRole(userRole);
            testUser.add(user);
        }
    }

    @Test
    public void add() {
        for (int i = 0; i < testUser.size(); i++) {
            Assertions.assertEquals("User " + i, testUser.get(i).getUsername());
            Assertions.assertEquals("password" + i, testUser.get(i).getPassword());
            Assertions.assertTrue(testUser.get(i).getRoles().contains(userRole));
        }
    }

    @Test
    public void update() {
        testUser.forEach(user -> {
            String newUsername = "User XXX";
            user.setUsername(newUsername);
            Assertions.assertEquals(newUsername, user.getUsername());

            String newPassword = "XXXX";
            user.setPassword(newPassword);
            Assertions.assertEquals(newPassword, user.getPassword());
        });
    }

    @Test
    public void emptyProblematicFields() {
        testUser.forEach(user -> {
            user.emptyRestrictedFields();
            Assertions.assertNull(user.getPassword());
        });
    }

}
