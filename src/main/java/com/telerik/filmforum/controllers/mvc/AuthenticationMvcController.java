package com.telerik.filmforum.controllers.mvc;

import com.telerik.filmforum.helpers.AuthenticationHelper;
import com.telerik.filmforum.models.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//TODO

@Controller
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "Login";
    }
}
