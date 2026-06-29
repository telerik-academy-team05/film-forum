package com.telerik.filmforum.repositories;

import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUserName(String userName) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, userName);
            if (user == null) {
                throw new EntityNotFoundException("User", "username", userName);
            }
            return user;
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }

    }

    @Override
    public void delete(int id) {
        User user = getById(id);
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        }
    }
}
