package com.telerik.filmforum.controllers.rest;

import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.exceptions.EntityDuplicateException;
import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.helpers.AuthenticationHelper;
import com.telerik.filmforum.helpers.AuthorizationHelper;
import com.telerik.filmforum.helpers.UserMapper;
import com.telerik.filmforum.models.Post;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.models.UserDto;
import com.telerik.filmforum.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final AuthorizationHelper authorizationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper,
                              AuthorizationHelper authorizationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.authorizationHelper = authorizationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            authorizationHelper.checkAccessPermissions(id, user);
            UserDto userDto = userMapper.toDto(userService.getUserById(id));
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto);
            userService.createUser(user);
            return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.CREATED);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestHeader HttpHeaders headers, @PathVariable int id,
                                              @Valid @RequestBody UserDto userDto) {
        try {
            User executingUser = authenticationHelper.tryGetUser(headers);
            authorizationHelper.checkAccessPermissions(id, executingUser);
            User user = userMapper.fromDto(id, userDto);
            userService.updateUser(user);
            return ResponseEntity.ok(userMapper.toDto(user));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User executingUser = authenticationHelper.tryGetUser(headers);
            authorizationHelper.checkAccessPermissions(id, executingUser);
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<User>> get(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable int id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @PutMapping("/{id}/promote")
    public ResponseEntity<UserDto> promoteUserToAdmin(@PathVariable int id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @PutMapping("/{id}/block")
    public ResponseEntity<UserDto> blockUser(@PathVariable int id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @PutMapping("/{id}/unblock")
    public ResponseEntity<UserDto> unblockUser(@PathVariable int id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
