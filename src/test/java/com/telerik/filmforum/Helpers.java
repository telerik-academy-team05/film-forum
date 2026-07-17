package com.telerik.filmforum;

import com.telerik.filmforum.models.Role;
import com.telerik.filmforum.models.RoleType;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.models.Post;

/**
 * Small helper class for tests.
 * It creates ready-to-use "fake" objects so each test does not
 * have to build a User or Role from scratch every time.
 */
public class Helpers {

    public static Role createMockUserRole() {
        Role role = new Role();
        role.setId(2);
        role.setRoleType(RoleType.USER);
        return role;
    }

    public static Role createMockAdminRole() {
        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.ADMIN);
        return role;
    }

    public static User createMockUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test.user@example.com");
        user.setPhoneNumber("555-0000");
        user.setBlocked(false);
        user.setRole(createMockUserRole());
        return user;
    }

    public static Post createMockPost() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("This is a valid post title");
        post.setContent("This is some valid post content for testing purposes.");
        post.setAuthor(createMockUser());
        return post;
    }
}
