package com.klymovych.tasktracker.service.Impl;

import com.klymovych.tasktracker.exception.NullEntityReferenceException;
import com.klymovych.tasktracker.model.User;
import com.klymovych.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.klymovych.tasktracker.repository.UserRepositoryTest.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void create_WithValidUser_ReturnsSavedUser() {
        User user = createUser();

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.create(user);

        Assertions.assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void readById_WithExistingId_ReturnsUser() {
        long userId = 1L;
        User user = createUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.readById(userId);

        Assertions.assertEquals(user, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void readById_WithNonExistingId_ThrowsEntityNotFoundException() {
        long nonExistingId = 100L;

        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.readById(nonExistingId));
        verify(userRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void update_WithValidUser_ReturnsUpdatedUser() {
        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.update(user);

        Assertions.assertEquals(user, updatedUser);
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_WithNullUser_ThrowsNullEntityReferenceException() {
        Assertions.assertThrows(NullEntityReferenceException.class, () -> userService.update(null));
        verifyNoInteractions(userRepository);
    }

    @Test
    void update_WithNonExistingUser_ThrowsEntityNotFoundException() {
        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.update(user));
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void delete_WithExistingId_DeletesUser() {
        long userId = 1L;
        User user = createUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void delete_WithNonExistingId_ThrowsEntityNotFoundException() {
        long nonExistingId = 100L;

        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.delete(nonExistingId));
        verify(userRepository, times(1)).findById(nonExistingId);
        verifyNoMoreInteractions(userRepository);
    }
}