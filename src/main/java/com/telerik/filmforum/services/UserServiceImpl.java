package com.telerik.filmforum.services;


import com.telerik.filmforum.exceptions.EntityDuplicateException;
import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.Role;
import com.telerik.filmforum.models.RoleType;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleService roleService) {
        this.userRepository = repository;
        this.roleService = roleService;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void createUser(User user) {
        if (usernameExists(user.getUsername())) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
        if (emailExists(user.getEmail())) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
        Role defaultRole = roleService.getRoleByType(RoleType.USER);
        user.setRole(defaultRole);
        userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user) {
        User currentUser = getUserById(user.getId());
        if (!currentUser.getEmail().equals(user.getEmail()) && emailExists(user.getEmail())) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    private boolean usernameExists(String username) {
        try {
            userRepository.getByUsername(username);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    private boolean emailExists(String email) {
        try {
            userRepository.getByEmail(email);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}
