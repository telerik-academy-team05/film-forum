package com.telerik.filmforum.controllers.rest;

import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.helpers.AuthenticationHelper;
import com.telerik.filmforum.helpers.AuthorizationHelper;
import com.telerik.filmforum.helpers.PostMapper;
import com.telerik.filmforum.models.*;
import com.telerik.filmforum.services.CommentService;
import com.telerik.filmforum.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Posts", description = "Operations for creating, viewing, updating and deleting posts")
@RestController
@RequestMapping("api/posts")
public class PostRestController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final CommentService commentService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostRestController(PostService postService, PostMapper postMapper, CommentService commentService, AuthenticationHelper authenticationHelper,
                              AuthorizationHelper authorizationHelper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
    }

    @Operation(summary = "Get all posts",
            description = "Returns all posts with optional filtering by title, author and sorting.")
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@Parameter(
                                                             name = "Authorization",
                                                             description = "Format: username password",
                                                             required = true,
                                                             in = ParameterIn.HEADER)
                                                     @RequestHeader HttpHeaders headers,
                                                     @RequestParam(required = false) String title,
                                                     @RequestParam(required = false) String author,
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(required = false) String sortOrder) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        PostFilters postFilters = new PostFilters(title, author, sortBy, sortOrder);
        List<PostDto> posts = postService.getAllPosts(postFilters)
                .stream()
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Get post by id",
            description = "Returns a single post by its id.")
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            PostDto postDto = postMapper.toDto(postService.getPostById(id));
            return ResponseEntity.ok(postDto);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Create post",
            description = "Creates a new post authored by the authenticated user.")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDto(postDto);
            postService.createPost(post, user);
            return new ResponseEntity<>(postDto, HttpStatus.CREATED);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @Operation(summary = "Update post",
            description = "Updates an existing post. Only the author or an admin can update it.")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestHeader HttpHeaders headers,
                                              @PathVariable int id,
                                              @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postMapper.fromDto(id, postDto);
            postService.updatePost(post, user);
            return ResponseEntity.ok(postMapper.toDto(post));
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Delete post",
            description = "Deletes a post. Only the author or an admin can delete it.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
            postService.deletePost(id, user);
            return ResponseEntity.noContent().build();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get post comments",
            description = "Returns all comments for a given post.")
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getPostComments(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            List<Comment> comments = commentService.getByPostId(id);
            return ResponseEntity.ok(comments);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Get posts count",
            description = "Returns the total number of posts created so far.")
    @GetMapping("/count")
    public ResponseEntity<Long> getPostsCount() {
        return ResponseEntity.ok(postService.getPostsCount());
    }

    @Operation(summary = "Get most commented posts",
            description = "Returns the top 10 most commented posts.")
    @GetMapping("/most-commented")
    public ResponseEntity<List<PostDto>> getMostCommentedPosts() {
        List<PostDto> posts = postService.getMostCommentedPosts()
                .stream()
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Get most recent posts",
            description = "Returns the 10 most recently created posts.")
    @GetMapping("/most-recent")
    public ResponseEntity<List<PostDto>> getMostRecentPosts() {
        List<PostDto> posts = postService.getMostRecentPosts()
                .stream()
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(posts);
    }

}



