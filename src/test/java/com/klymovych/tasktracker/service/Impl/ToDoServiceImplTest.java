package com.klymovych.tasktracker.service.Impl;

import com.klymovych.tasktracker.exception.NullEntityReferenceException;
import com.klymovych.tasktracker.model.ToDo;
import com.klymovych.tasktracker.repository.ToDoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ToDoServiceImplTest {

    @Mock
    private ToDoRepository todoRepository;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateToDo() {
        ToDo todo = new ToDo();
        when(todoRepository.save(any(ToDo.class))).thenReturn(todo);

        ToDo createdTodo = toDoService.create(todo);

        verify(todoRepository, times(1)).save(todo);

        Assertions.assertEquals(todo, createdTodo);
    }

    @Test
    public void testReadById_existingId() {
        long id = 1L;
        ToDo todo = new ToDo();
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        ToDo retrievedTodo = toDoService.readById(id);

        verify(todoRepository, times(1)).findById(id);

        Assertions.assertEquals(todo, retrievedTodo);
    }

    @Test
    public void testReadById_nonExistingId() {
        long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            toDoService.readById(id);
        });

        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateToDo() {
        ToDo todo = new ToDo();
        todo.setId(1L);

        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(ToDo.class))).thenReturn(todo);

        ToDo updatedTodo = toDoService.update(todo);

        verify(todoRepository, times(1)).findById(todo.getId());
        verify(todoRepository, times(1)).save(todo);

        Assertions.assertEquals(todo, updatedTodo);
    }

    @Test
    public void testUpdateToDo_nullToDo() {
        Assertions.assertThrows(NullEntityReferenceException.class, () -> {
            toDoService.update(null);
        });

    }

    @Test
    public void testDeleteToDo() {
        long id = 1L;
        ToDo todo = new ToDo();
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        toDoService.delete(id);

        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).delete(todo);
    }

    @Test
    public void testDeleteNonExistentToDo() {
        long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            toDoService.delete(id);
        });

        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, never()).delete(any());
    }

    @Test
    public void testGetAllToDos_notEmptyList() {
        List<ToDo> todos = new ArrayList<>();
        todos.add(new ToDo());

        when(todoRepository.findAll()).thenReturn(todos);

        List<ToDo> retrievedTodos = toDoService.getAll();

        verify(todoRepository, times(1)).findAll();

        Assertions.assertEquals(todos, retrievedTodos);
    }

    @Test
    public void testGetAllToDos_emptyList() {
        when(todoRepository.findAll()).thenReturn(new ArrayList<>());

        List<ToDo> retrievedTodos = toDoService.getAll();

        verify(todoRepository, times(1)).findAll();

        Assertions.assertEquals(new ArrayList<>(), retrievedTodos);
    }

    @Test
    public void testGetByUserId_notEmptyList() {
        long userId = 1L;
        List<ToDo> todos = new ArrayList<>();
        todos.add(new ToDo());

        when(todoRepository.getByUserId(userId)).thenReturn(todos);

        List<ToDo> retrievedTodos = toDoService.getByUserId(userId);

        verify(todoRepository, times(1)).getByUserId(userId);

        Assertions.assertEquals(todos, retrievedTodos);
    }

    @Test
    public void testGetByUserId_emptyList() {
        long userId = 1L;
        when(todoRepository.getByUserId(userId)).thenReturn(new ArrayList<>());

        List<ToDo> retrievedTodos = toDoService.getByUserId(userId);

        verify(todoRepository, times(1)).getByUserId(userId);

        Assertions.assertEquals(new ArrayList<>(), retrievedTodos);
    }

}