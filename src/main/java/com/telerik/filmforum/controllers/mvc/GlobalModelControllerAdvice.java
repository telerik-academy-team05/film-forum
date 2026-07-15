package com.telerik.filmforum.controllers.mvc;

import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelControllerAdvice {

    private final UserService userService;

    @Autowired
    public GlobalModelControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) {
            return null;
        }

        try {
            return userService.getByUsername(username);
        } catch (EntityNotFoundException e) {
            session.removeAttribute("currentUser");
            return null;
        }
    }
}
