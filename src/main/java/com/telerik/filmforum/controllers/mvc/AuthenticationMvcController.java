package com.telerik.filmforum.controllers.mvc;

import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.exceptions.EntityDuplicateException;
import com.telerik.filmforum.helpers.AuthenticationHelper;
import com.telerik.filmforum.helpers.UserMapper;
import com.telerik.filmforum.models.LoginDto;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.models.UserDto;
import com.telerik.filmforum.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserMapper userMapper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userService = userService;

    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") LoginDto loginDto, HttpSession session, Model model) {
        try {
            User user = authenticationHelper.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            session.setAttribute("currentUser", user.getUsername());
            return "redirect:/";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "LoginView";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult bindingResult,
                           HttpSession session,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }
        try {
            User user = userMapper.fromDto(userDto);
            userService.createUser(user);
            session.setAttribute("currentUser", user.getUsername());
            return "redirect:/";
        }catch (EntityDuplicateException e){
            model.addAttribute("error", e.getMessage());
            return "RegisterView";        }
    }
}
