package com.telerik.filmforum.services;

import com.telerik.filmforum.models.User;
import com.telerik.filmforum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public User getByUsername(String userName) {
        return repository.getByUsername(userName);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public void create(User user) {
        repository.create(user);
    }

    @Override
    public void update(User user) {
        repository.update(user);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
