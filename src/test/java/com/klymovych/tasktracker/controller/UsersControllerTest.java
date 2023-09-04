package com.klymovych.tasktracker.controller;

import com.klymovych.tasktracker.TaskTrackerApplication;
import com.klymovych.tasktracker.model.Role;
import com.klymovych.tasktracker.model.User;
import com.klymovych.tasktracker.service.RoleService;
import com.klymovych.tasktracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = TaskTrackerApplication.class)
public class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    private List<User> users;


    public static List<User> createUsers() {
        Role role2 = new Role();
        role2.setName("User");
        User user1 = new User(){{setFirstName("First1"); setLastName("Last1");setRole(role2);}};
        User user2 = new User(){{setFirstName("First1"); setLastName("Last2");setRole(role2);}};
        return Arrays.asList(user1, user2);
    }

    @BeforeEach
    public void setup() {
        users = createUsers();
        given(userService.readById(anyLong())).willReturn(users.get(0));
        given(userService.getAll()).willReturn(users);

        Role role2 = new Role();
        role2.setName("User");

        given(userService.create(any(User.class))).willAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(3L);
            user.setRole(role2);
            return user;
        });



        given(roleService.readById(2L)).willReturn(role2);
    }

    @Test
    public void create_ShouldReturnCreateUserView() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-user"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void create_WithValidUser_ShouldRedirectToUserInfo() throws Exception {
        mockMvc.perform(post("/users/create")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/all/users/3"));
    }
    @Test
    public void create_WithInvalidUser_ShouldReturnCreateUserView() throws Exception {
        mockMvc.perform(post("/users/create")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "invalid-email")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().hasErrors());
    }

    @Test
    public void read_ShouldReturnUserInfoView() throws Exception {
        mockMvc.perform(get("/users/{id}/read", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("user-info"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void update_ShouldReturnUpdateUserView() throws Exception {
        mockMvc.perform(get("/users/{id}/update", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("update-user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    public void update_WithValidUser_ShouldRedirectToUserInfo() throws Exception {
        mockMvc.perform(post("/users/{id}/update", 1L)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("roleId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/1/read"));
    }


    @Test
    public void update_WithInvalidUser_ShouldReturnUpdateUserView() throws Exception {
        mockMvc.perform(post("/users/{id}/update", 1L)
                        .param("firstName", "John")
                        .param("roleId", "2"))  // Provide a value for roleId parameter
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/1/read"));
    }

    @Test
    public void delete_ShouldRedirectToAllUsers() throws Exception {
        mockMvc.perform(get("/users/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));
    }

    @Test
    public void getAll_ShouldReturnUsersListView() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("users-list"))
                .andExpect(model().attributeExists("users"));
    }

}