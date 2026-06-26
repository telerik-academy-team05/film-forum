package com.telerik.filmforum.controllers.rest;

import com.telerik.filmforum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")

public class UserRestController {

    private UserService service;

    @Autowired
    public UserRestController (UserService service){
        this.service = service;
    }
}
