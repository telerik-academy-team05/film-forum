package com.telerik.filmforum.controllers.mvc;

import com.telerik.filmforum.models.UserDto;
import com.telerik.filmforum.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeMvcController {

    private final PostService postService;

    @Autowired
    public HomeMvcController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("postsCount", postService.getPostsCount());
        model.addAttribute("mostCommentedPosts", postService.getMostCommentedPosts());
        model.addAttribute("mostRecentPosts", postService.getMostRecentPosts());
        return "index";
    }

}
