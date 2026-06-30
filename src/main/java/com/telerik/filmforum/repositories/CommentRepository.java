package com.telerik.filmforum.repositories;

import com.telerik.filmforum.models.Comment;

import java.util.List;

public interface CommentRepository {
    Comment getById(int id);
    List<Comment> getAll();
    List<Comment> getByPostId(int postId);
    void create(Comment comment);
    void update(Comment comment);
    void delete(int id);
}
