package com.telerik.filmforum.controllers.mvc;

import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.helpers.AuthenticationHelper;
import com.telerik.filmforum.helpers.PostMapper;
import com.telerik.filmforum.models.*;
import com.telerik.filmforum.services.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;

    @Autowired
    public PostMvcController(PostService postService, AuthenticationHelper authenticationHelper, PostMapper postMapper) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
    }

    @GetMapping
    public String listPosts(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String author,
                            @RequestParam(required = false) String sortBy,
                            @RequestParam(required = false) String sortOrder,
                            Model model) {
        PostFilters filters = new PostFilters(title, author, sortBy, sortOrder);
        model.addAttribute("posts", postService.getAllPosts(filters));
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        return "PostListView";
    }

    @GetMapping("/{id}")
    public String postDetails(@PathVariable int id, Model model) {
        try {
            model.addAttribute("post", postService.getPostById(id));
            return "PostDetailsView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(HttpSession session, Model model) {
        if (!requireLogin(session, model)) {
            return "LoginView";
        }
        model.addAttribute("postDto", new PostDto());
        return "PostFormView";
    }

    @PostMapping
    public String createPost(@Valid @ModelAttribute("postDto") PostDto postDto,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "PostFormView";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Post post = postMapper.fromDto(postDto);
            postService.createPost(post, user);
            return "redirect:/posts/" + post.getId();
        }catch (AuthorizationException e){
            model.addAttribute("error", e.getMessage());
            return "PostFormView";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm (@PathVariable int id, HttpSession session, Model model){
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Post post = postService.getPostById(id);
            if (!canModifyPost(post,user)){
                throw new AuthorizationException("You are not authorized to edit this post");
            }
            model.addAttribute("post", post);
            model.addAttribute("postDto", postMapper.toDto(post));
            return "PostFormView";
        }catch (AuthorizationException | EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/edit")
    public String updatePost (@PathVariable int id, @Valid @ModelAttribute("postDto") PostDto postDto,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model){
        if (bindingResult.hasErrors()){
            return "PostFormView";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Post post = postMapper.fromDto(id, postDto);
            postService.updatePost(post,user);
            return "redirect:/posts/" + id;
        }catch (AuthorizationException | EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/delete")
    public String deletePost (@PathVariable int id, HttpSession session, Model model){
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Post post = postService.getPostById(id);
            postService.deletePost(id, user);
            return "redirect:/posts";
        }catch (AuthorizationException | EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    private boolean requireLogin(HttpSession session, Model model) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            return true;
        } catch (AuthorizationException e) {
            model.addAttribute("login", new LoginDto());
            model.addAttribute("error", "Please log in first");
            return false;
        }
    }

    private boolean canModifyPost(Post post, User user) {
        return post.getAuthor().getId() == user.getId();
    }
}
