package com.klymovych.tasktracker.controller;

import com.klymovych.tasktracker.model.ToDo;
import com.klymovych.tasktracker.service.TaskService;
import com.klymovych.tasktracker.service.ToDoService;
import com.klymovych.tasktracker.service.UserService;
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
public class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoService toDoService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private ToDo testToDo;


    @BeforeEach
    public void setup() {
        testToDo = toDoService.readById(7L);

    }

    @Test
    void create_ShouldReturnCreateToDoView() throws Exception {
        mockMvc.perform(get("/todos/create/users/{owner_id}" , 4L))
                .andExpect(status().isOk())
                .andExpect(view().name("create-todo"))
                .andExpect(model().attributeExists("todo"));
    }

    @Test
    void create_WithValidToDo_ShouldRedirectToAllToDo() throws Exception {
        mockMvc.perform(post("/todos/create/users/{owner_id}", 4L)
                .param("title" , "Test Task"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/all/users/4" ));
    }

    @Test
    void create_WithInvalidToDo_ShouldReturnCreateToDoView() throws Exception {
        mockMvc.perform(post("/todos/create/users/{owner_id}", 4L)
                .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("create-todo"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().hasErrors());
    }

    @Test
    void read_ShouldReturnToDoTasksViev() throws Exception {
        mockMvc.perform(get("/todos/{id}/tasks", 7L))
                .andExpect(status().isOk())
                .andExpect(view().name("todo-tasks"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void update_ShouldReturnUpdateToDoView() throws Exception {
        mockMvc.perform(get("/todos/{todo_id}/update/users/{owner_id}", 7L, 4L))
                .andExpect(status().isOk())
                .andExpect(view().name("update-todo"))
                .andExpect(model().attributeExists("todo"));
    }

    @Test
    void update_WithValidToDo_ShouldRedirectToToDosAll() throws Exception {
        testToDo.setTitle("Test");
        mockMvc.perform(post("/todos/{todo_id}/update/users/{owner_id}", 7L, 4L)
                        .flashAttr("todo", testToDo))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/all/users/4"));
    }

    @Test
    void delete_ShouldRedirectToAllToDos() throws Exception {
        mockMvc.perform(get("/todos/{todo_id}/delete/users/{owner_id}", 10L, 5L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/all/users/5"));
    }

    @Test
    void getAll_ShouldReturnToDosListView() throws Exception {
        mockMvc.perform(get("/todos/all/users/{user_id}", 4L))
                .andExpect(status().isOk())
                .andExpect(view().name("todos-user"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void addCollaborator_ShouldRedirectToTaskInfo() throws Exception {
        mockMvc.perform(get("/todos/{id}/add", 7L)
                        .param("user_id", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/7/tasks"));
    }

    @Test
    public void removeCollaborator_ShouldRedirectToToDoTasks() throws Exception {
        mockMvc.perform(get("/todos/{id}/remove", 7L)
                        .param("user_id", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/7/tasks"));
    }

}