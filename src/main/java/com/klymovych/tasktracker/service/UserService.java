package com.klymovych.tasktracker.service;

import com.klymovych.tasktracker.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User readById(long id);
    User update(User user);
    void delete(long id);
    List<User> getAll();

    User getByEmail(String userName);

}