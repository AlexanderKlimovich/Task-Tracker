package com.klymovych.tasktracker.service.Impl;

import com.klymovych.tasktracker.model.Task;
import com.klymovych.tasktracker.model.ToDo;
import com.klymovych.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    public Task createTask(){
        Task task = new Task();
        task.setId(10);
        task.setName("Make coffe");
        return task;
    }

    @Test
    void create() {
        Task task = createTask();

        when(taskRepository.save(task)).thenReturn(task);

        Task taskAfterSave = taskService.create(task);

        assertEquals(task, taskAfterSave);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void readById() {
        long taskId = 10;
        Task task = createTask();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));

        Task taskAfterFind = taskService.readById(10);

        Assert.isTrue(taskAfterFind.getId() == 10);
    }

    @Test
    void update() {
        Task task = createTask();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task taskAfterUpdate = taskService.update(task);

        assertEquals(task, taskAfterUpdate);
        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void delete() {
        long taskId = 10;
        Task task = createTask();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));

        taskService.delete(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void getAll() {
        Task taskForTest = createTask();
        Task taskOne = new Task();
        taskOne.setId(25);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(taskForTest,taskOne));

        List<Task> taskList = taskService.getAll();
        Assert.isTrue(taskList.size() == 2);
        Assert.isTrue(taskList.get(0).getId() == 10);
    }

    @Test
    void getByTodoId() {
        ToDo todo = new ToDo();
        todo.setId(13);
        long todoId = 13;
        Task taskForTest = createTask();
        Task taskOne = new Task();
        taskOne.setId(25);
        todo.setTasks(Arrays.asList(taskForTest,taskOne));

        when(taskRepository.getByTodoId(todoId)).thenReturn(todo.getTasks());

        List<Task> taskList = taskService.getByTodoId(todoId);
        Assert.isTrue(taskList.size() == 2);
        Assert.isTrue(taskList.get(0).getId() == 10);
    }
}