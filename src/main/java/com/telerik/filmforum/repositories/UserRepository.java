package com.telerik.filmforum.repositories;

import com.telerik.filmforum.models.User;

import java.util.List;

public interface UserRepository {

    User getById(int id);

    User getByUsername(String userName);

    List<User> getAll();

    void create(User user);

    void update(User user);

    void delete(int id);
}
