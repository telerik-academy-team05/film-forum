package com.telerik.filmforum.repositories;

import com.telerik.filmforum.models.User;
import com.telerik.filmforum.models.UserFilters;

import java.util.List;

public interface UserRepository {

    User getUserById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getFilteredUsers(UserFilters userFilters);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}

