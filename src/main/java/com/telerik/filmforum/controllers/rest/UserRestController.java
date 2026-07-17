package com.telerik.filmforum.controllers.rest;

import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.exceptions.EntityDuplicateException;
import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.helpers.AuthenticationHelper;
import com.telerik.filmforum.helpers.AuthorizationHelper;
import com.telerik.filmforum.helpers.UserMapper;
import com.telerik.filmforum.models.*;
import com.telerik.filmforum.services.RoleService;
import com.telerik.filmforum.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Users", description = "Operations for user registration, profile management and admin actions")
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

    @Operation(summary = "Get user by id",
            description = "Returns a single user by id. Accessible by the user themselves or an admin.")
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

    @Operation(summary = "Register user",
            description = "Creates a new user account.")
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

    @Operation(summary = "Update user",
            description = "Updates a user's profile. Accessible by the user themselves or an admin.")
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

    @Operation(summary = "Delete user",
            description = "Deletes a user. Accessible by the user themselves or an admin.")
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

    @Operation(summary = "Search users",
            description = "Returns users with optional filtering by username, email, first name and sorting. Admin only.")
    @GetMapping
    public ResponseEntity<List<UserDto>> getFilteredUsers(@RequestHeader HttpHeaders headers,
                                                          @RequestParam(required = false) String username,
                                                          @RequestParam(required = false) String email,
                                                          @RequestParam(required = false) String firstName,
                                                          @RequestParam(required = false) String sortBy,
                                                          @RequestParam(required = false) String sortOrder) {


        try {
            User user = authenticationHelper.tryGetUser(headers);
            authorizationHelper.isAdmin(user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        UserFilters userFilters = new UserFilters(username, email, firstName, sortBy, sortOrder);
        List<UserDto> users = userService.getFilteredUsers(userFilters)
                .stream()
                .map(userMapper::toDto)
                .toList();

        return ResponseEntity.ok(users);
    }


    @Operation(summary = "Get user's posts",
            description = "Returns all posts created by the given user.")
    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable int id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "Promote user to admin",
            description = "Promotes a normal user to admin. Admin only.")
    @PutMapping("/{id}/promote")
    public ResponseEntity<UserDto> promoteUserToAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User adminUser = authenticationHelper.tryGetUser(headers);
            authorizationHelper.isAdmin(adminUser);
            User promotedUser = userService.promoteUserToAdmin(id);

            return ResponseEntity.ok(userMapper.toDto(promotedUser));

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @Operation(summary = "Block user",
            description = "Blocks a user so they cannot create posts or comments. Admin only.")
    @PutMapping("/{id}/block")
    public ResponseEntity<UserDto> blockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User adminUser = authenticationHelper.tryGetUser(headers);
            authorizationHelper.isAdmin(adminUser);
            User userToBlock = userService.blockUser(id);

            return ResponseEntity.ok(userMapper.toDto(userToBlock));

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @Operation(summary = "Unblock user",
            description = "Unblocks a previously blocked user. Admin only.")
    @PutMapping("/{id}/unblock")
    public ResponseEntity<UserDto> unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User adminUser = authenticationHelper.tryGetUser(headers);
            authorizationHelper.isAdmin(adminUser);
            User userToUnblock = userService.unblockUser(id);

            return ResponseEntity.ok(userMapper.toDto(userToUnblock));

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
