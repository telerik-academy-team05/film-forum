package com.telerik.filmforum.services;

import com.telerik.filmforum.models.User;

import java.util.List;

public interface UserService {

    User getById(int id);

    User getByName(String name);

    List<User> getAll();

    void create(User user);

    void update(User user);

    void delete(int id);
}
