package com.klymovych.tasktracker.controller;

import com.klymovych.tasktracker.dto.TaskDto;
import com.klymovych.tasktracker.service.ToDoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private TaskDto taskDto;

    @Autowired
    private ToDoService toDoService;

    @BeforeEach
    public void setup() {
        this.taskDto = new TaskDto();
        taskDto.setName("Task #2");
        taskDto.setPriority("LOW");
        taskDto.setTodoId(7L);
        taskDto.setStateId(5L);
    }


    @Test
    void create_ShouldReturnCreateTaskView() throws Exception {
        mockMvc.perform(get("/tasks/create/todos/{todo_id}" , 7L))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("priorities"));
    }

    @Test
    void create_WithValidTask_ShouldRedirectToAllTasksFromToDo() throws Exception {
        mockMvc.perform(post("/tasks/create/todos/{todo_id}", 7L)
                        .flashAttr("task", taskDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/7/tasks"));
    }

    @Test
    void create_WithInvalidTask_ShouldReturnCreateTaskView() throws Exception {
        TaskDto taskDto = new TaskDto();
        mockMvc.perform(post("/tasks/create/todos/{todo_id}", 7L)
                        .flashAttr("task", taskDto))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().hasErrors());
    }

    @Test
    void update_ShouldReturnUpdateTaskView() throws Exception {
        mockMvc.perform(get("/tasks/{task_id}/update/todos/{todo_id}", 5L, 7L))
                .andExpect(status().isOk())
                .andExpect(view().name("update-task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("priorities"))
                .andExpect(model().attributeExists("states"));
    }

    @Test
    void update_WithValidToDo_ShouldRedirectToToDosAll() throws Exception {
        TaskDto task = taskDto;
        task.setId(5L);
        task.setName("Test Task");
                mockMvc.perform(post("/tasks/{task_id}/update/todos/{todo_id}", 5L, 7L)
                        .flashAttr("task", task))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/todos/7/tasks"));
    }

    @Test
    void delete_ShouldRedirectToToDosAll() throws Exception {
        mockMvc.perform(get("/tasks/{task_id}/delete/todos/{todo_id}", 5L, 7L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/7/tasks"));
    }
}