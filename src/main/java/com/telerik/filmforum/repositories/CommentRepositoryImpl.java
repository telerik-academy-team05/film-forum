package com.telerik.filmforum.repositories;

import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Comment getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.find(Comment.class, id);
            if (comment == null) {
                throw new EntityNotFoundException("Comment", id);
            }
            return comment;
        }
    }

    @Override
    public List<Comment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Comment", Comment.class).list();
        }
    }

    // Нов полезен метод: Вземане на коментари за конкретен пост чрез HQL заявка
    @Override
    public List<Comment> getByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery(
                    "from Comment where post.id = :postId", Comment.class);
            query.setParameter("postId", postId);
            return query.list();
        }
    }

    @Override
    public void create(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Comment comment = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(comment);
            session.getTransaction().commit();
        }
    }
}