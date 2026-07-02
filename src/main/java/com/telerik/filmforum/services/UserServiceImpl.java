package com.telerik.filmforum.services;


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
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void createUser (User user) {
        Role defaultRole = roleService.getRoleByType(RoleType.USER);
        user.setRole(defaultRole);
        userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser (int id) {
        userRepository.deleteUser(id);
    }


}
