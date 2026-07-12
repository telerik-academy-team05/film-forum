package com.telerik.filmforum.controllers.rest;

import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.Comment;
import com.telerik.filmforum.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable int id) {
        try {
            return commentService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getByPostId(@PathVariable int postId) {
        return commentService.getByPostId(postId);
    }

    @PostMapping
    public void create(@RequestBody Comment comment) {
        commentService.create(comment);
    }

    @PutMapping
    public void update(@RequestBody Comment comment) {
        commentService.update(comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        commentService.delete(id);
    }
}
