package com.telerik.filmforum.repositories;

import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.Post;
import com.telerik.filmforum.models.PostFilters;
import com.telerik.filmforum.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Post getPostById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.find(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public List<Post> getPostsByAuthor(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "from Post where author.id = :authorId", Post.class);
            query.setParameter("authorId", user.getId());
            return query.list();
        }
    }

    @Override
    public List<Post> getAllPosts(PostFilters postFilters) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            postFilters.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            postFilters.getAuthor().ifPresent(value -> {
                filters.add("author.username like :author");
                params.put("author", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(postFilters));
            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public void createPost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updatePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deletePost(int id) {
        Post post = getPostById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(post);
            session.getTransaction().commit();
        }

    }

    @Override
    public long getPostsCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(p) from Post p", Long.class);
            return query.getSingleResult();
        }
    }

    @Override
    public List<Post> getMostCommentedPosts() {
        try(Session session = sessionFactory.openSession()){
            Query<Post> query = session.createQuery(
                    "select p from Post p left join p.comments c group by p order by count(c) desc", Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public List<Post> getMostRecentPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "from Post order by createdAt desc", Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    private String generateOrderBy(PostFilters postFilters) {
        if (postFilters.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy;

        switch (postFilters.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "createdAt":
                orderBy = "createdAt";
                break;
            case "author":
                orderBy = "author.username";
                break;
            default:
                return "";
        }

        String sortOrder = "";
        if (postFilters.getSortOrder().isPresent()
                && postFilters.getSortOrder().get().equalsIgnoreCase("desc")) {
            sortOrder = " desc";
        }

        return String.format(" order by %s%s", orderBy, sortOrder);

    }
}
