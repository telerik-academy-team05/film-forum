package com.telerik.filmforum.controllers.mvc;

import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.exceptions.EntityDuplicateException;
import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.helpers.AuthenticationHelper;
import com.telerik.filmforum.helpers.AuthorizationHelper;
import com.telerik.filmforum.helpers.UserMapper;
import com.telerik.filmforum.models.LoginDto;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.models.UserDto;
import com.telerik.filmforum.models.UserFilters;
import com.telerik.filmforum.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final AuthorizationHelper authorizationHelper;
    private final UserMapper userMapper;

    public UserMvcController(UserService userService,
                             AuthenticationHelper authenticationHelper,
                             AuthorizationHelper authorizationHelper,
                             UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.authorizationHelper = authorizationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public String profile(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("user", user);
            return "ProfileView";
        } catch (AuthorizationException e) {
            model.addAttribute("login", new LoginDto());
            model.addAttribute("error", "Please log in first.");
            return "LoginView";
        }
    }

    @GetMapping("/me/edit")
    public String editProfile(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            UserDto userDto = userMapper.toDto(user);
            userDto.setPassword(user.getPassword());
            model.addAttribute("user", user);
            model.addAttribute("userDto", userDto);
            return "UserFormView";
        } catch (AuthorizationException e) {
            model.addAttribute("login", new LoginDto());
            model.addAttribute("error", "Please log in first.");
            return "LoginView";
        }
    }

    @PostMapping("/me/edit")
    public String updateProfile(@Valid @ModelAttribute UserDto userDto,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            if (bindingResult.hasErrors()) {
                model.addAttribute("user", currentUser);
                return "UserFormView";
            }

            User user = userMapper.fromDto(currentUser.getId(), userDto);
            userService.updateUser(user);
            return "redirect:/users/me";
        } catch (AuthorizationException e) {
            model.addAttribute("login", new LoginDto());
            model.addAttribute("error", "Please log in first.");
            return "LoginView";
        } catch (EntityDuplicateException e) {
            model.addAttribute("error", e.getMessage());
            return "UserFormView";
        }
    }

    @GetMapping
    public String users(@RequestParam(required = false) String username,
                        @RequestParam(required = false) String email,
                        @RequestParam(required = false) String firstName,
                        @RequestParam(required = false) String sortBy,
                        @RequestParam(required = false) String sortOrder,
                        HttpSession session,
                        Model model) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);
            authorizationHelper.isAdmin(admin);
            UserFilters filters = new UserFilters(username, email, firstName, sortBy, sortOrder);
            model.addAttribute("users", userService.getFilteredUsers(filters));
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("firstName", firstName);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortOrder", sortOrder);
            return "UserListView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/promote")
    public String promote(@PathVariable int id, HttpSession session, Model model) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);
            authorizationHelper.isAdmin(admin);
            userService.promoteUserToAdmin(id);
            return "redirect:/users";
        } catch (AuthorizationException | EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/block")
    public String block(@PathVariable int id, HttpSession session, Model model) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);
            authorizationHelper.isAdmin(admin);
            userService.blockUser(id);
            return "redirect:/users";
        } catch (AuthorizationException | EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/unblock")
    public String unblock(@PathVariable int id, HttpSession session, Model model) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);
            authorizationHelper.isAdmin(admin);
            userService.unblockUser(id);
            return "redirect:/users";
        } catch (AuthorizationException | EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session, Model model) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);
            authorizationHelper.isAdmin(admin);
            userService.deleteUser(id);
            return "redirect:/users";
        } catch (AuthorizationException | EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
