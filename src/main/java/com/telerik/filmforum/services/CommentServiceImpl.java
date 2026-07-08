package com.telerik.filmforum.services;

import com.telerik.filmforum.models.Comment;
import com.telerik.filmforum.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment getById(int id) {
        return commentRepository.getById(id);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Override
    public List<Comment> getByPostId(int postId) {
        return commentRepository.getByPostId(postId);
    }

    @Override
    public void create(Comment comment) {
        commentRepository.create(comment);
    }

    @Override
    public void update(Comment comment) {
        commentRepository.update(comment);
    }

    @Override
    public void delete(int id) {
        commentRepository.delete(id);
    }
}
