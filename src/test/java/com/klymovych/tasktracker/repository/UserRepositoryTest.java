package com.klymovych.tasktracker.repository;

import com.klymovych.tasktracker.TaskTrackerApplication;
import com.klymovych.tasktracker.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = TaskTrackerApplication.class)
public class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    public static User createUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        return user;
    }

    @Test
    public void testDelete() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);

        userRepository.delete(savedUser);

        User deletedUser = userRepository.findById(savedUser.getId()).orElse(null);

        assertNull(deletedUser);
    }


    @Test
    public void testCreate() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    public void testReadById() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        User savedUser = userRepository.save(user);

        User retrievedUser = userRepository.findById(savedUser.getId()).orElse(null);

        assertNotNull(retrievedUser);
        assertEquals(savedUser.getId(), retrievedUser.getId());
        assertEquals(savedUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(savedUser.getLastName(), retrievedUser.getLastName());
        assertEquals(savedUser.getEmail(), retrievedUser.getEmail());
        assertEquals(savedUser.getPassword(), retrievedUser.getPassword());
    }


}