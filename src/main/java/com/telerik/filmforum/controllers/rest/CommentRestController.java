package com.telerik.filmforum.controllers.rest;

import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.Comment;
import com.telerik.filmforum.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get comment by id",
            description = "Returns a single comment by its id.")
    @GetMapping("/{id}")
    public Comment getById(@PathVariable int id) {
        try {
            return commentService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get all comments",
            description = "Returns all comments in the system.")
    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @Operation(summary = "Get comments by post",
            description = "Returns all comments for a given post.")
    @GetMapping("/post/{postId}")
    public List<Comment> getByPostId(@PathVariable int postId) {
        return commentService.getByPostId(postId);
    }

    @Operation(summary = "Create comment",
            description = "Creates a new comment.")
    @PostMapping
    public void create(@RequestBody Comment comment) {
        commentService.create(comment);
    }

    @Operation(summary = "Update comment",
            description = "Updates an existing comment.")
    @PutMapping
    public void update(@RequestBody Comment comment) {
        commentService.update(comment);
    }

    @Operation(summary = "Delete comment",
            description = "Deletes a comment by its id.")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        commentService.delete(id);
    }
}
