package com.telerik.filmforum.repositories;

import com.telerik.filmforum.models.User;

import java.util.List;

public interface UserRepository {

    User getUserById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getAllUsers();

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}

