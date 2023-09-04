package com.klymovych.tasktracker.controller;

import com.klymovych.tasktracker.TaskTrackerApplication;
import com.klymovych.tasktracker.model.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
@ContextConfiguration(classes = TaskTrackerApplication.class)
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    public static List<User> createUsers() {
        User user1 = new User() {{
            setFirstName("First1");
            setLastName("Last1");
        }};
        User user2 = new User() {{
            setFirstName("First1");
            setLastName("Last2");
        }};
        return Arrays.asList(user1, user2);
    }

    List<User> users;

    @BeforeEach
    public void setup() {
        users = createUsers();
        given(userService.getAll()).willReturn(users);
    }

    @Test
    public void home_ShouldAddUsersToModelAndReturnHomeView() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", users));

    }
}