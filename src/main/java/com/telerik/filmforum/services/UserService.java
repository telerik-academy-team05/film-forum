package com.telerik.filmforum.services;

import com.telerik.filmforum.models.User;

import java.util.List;

public interface UserService {

    User getUserById(int id);

    User getByUsername(String username);

    List<User> getAllUsers();

    void createUser (User user);

    void updateUser (User user);

    void deleteUser (int id);

}
