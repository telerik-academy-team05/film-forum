package com.telerik.filmforum.services;

import com.telerik.filmforum.models.User;
import com.telerik.filmforum.models.UserDto;
import com.telerik.filmforum.models.UserFilters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface UserService {

    User getUserById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getFilteredUsers(UserFilters userFilters);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);

    User promoteUserToAdmin(int id);

    User blockUser(int id);

    User unblockUser(int id);

}
