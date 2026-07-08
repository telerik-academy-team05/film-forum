package com.telerik.filmforum.services;

import com.telerik.filmforum.Helpers;
import com.telerik.filmforum.exceptions.EntityDuplicateException;
import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.Role;
import com.telerik.filmforum.models.RoleType;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @InjectMocks
    UserServiceImpl userService;


    @Test
    public void getUserById_should_returnUser_when_userExists() {
        User mockUser = Helpers.createMockUser();
        when(userRepository.getUserById(1)).thenReturn(mockUser);

        User result = userService.getUserById(1);

        assertEquals(mockUser, result);
    }

    @Test
    public void getUserById_should_throw_when_userDoesNotExist() {
        when(userRepository.getUserById(anyInt()))
                .thenThrow(new EntityNotFoundException("User", 1));

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1));
    }


    @Test
    public void createUser_should_callRepository_when_usernameAndEmailAreFree() {
        User mockUser = Helpers.createMockUser();
        Role userRole = Helpers.createMockUserRole();

        when(userRepository.getByUsername(mockUser.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", mockUser.getUsername()));
        when(userRepository.getByEmail(mockUser.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "email", mockUser.getEmail()));
        when(roleService.getRoleByType(RoleType.USER)).thenReturn(userRole);

        userService.createUser(mockUser);

        verify(userRepository, times(1)).createUser(mockUser);
        assertEquals(userRole, mockUser.getRole());
    }

    @Test
    public void createUser_should_throw_when_usernameAlreadyExists() {
        User mockUser = Helpers.createMockUser();
        when(userRepository.getByUsername(mockUser.getUsername())).thenReturn(mockUser);

        assertThrows(EntityDuplicateException.class, () -> userService.createUser(mockUser));

        verify(userRepository, never()).createUser(any(User.class));
    }

    @Test
    public void createUser_should_throw_when_emailAlreadyExists() {
        User mockUser = Helpers.createMockUser();
        when(userRepository.getByUsername(mockUser.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", mockUser.getUsername()));
        when(userRepository.getByEmail(mockUser.getEmail())).thenReturn(mockUser);

        assertThrows(EntityDuplicateException.class, () -> userService.createUser(mockUser));
        verify(userRepository, never()).createUser(any(User.class));
    }


    @Test
    public void updateUser_should_callRepository_when_emailIsUnchanged() {
        User mockUser = Helpers.createMockUser();
        when(userRepository.getUserById(mockUser.getId())).thenReturn(mockUser);

        userService.updateUser(mockUser);

        verify(userRepository, times(1)).updateUser(mockUser);
    }

    @Test
    public void updateUser_should_throw_when_newEmailBelongsToSomeoneElse() {
        User currentUser = Helpers.createMockUser();

        User updatedUser = Helpers.createMockUser();
        updatedUser.setEmail("new.email@example.com");

        when(userRepository.getUserById(updatedUser.getId())).thenReturn(currentUser);
        when(userRepository.getByEmail("new.email@example.com")).thenReturn(new User());

        assertThrows(EntityDuplicateException.class, () -> userService.updateUser(updatedUser));
        verify(userRepository, never()).updateUser(any(User.class));
    }


    @Test
    public void deleteUser_should_callRepository() {
        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteUser(1);
    }


    @Test
    public void promoteUserToAdmin_should_setAdminRole_and_save() {
        User mockUser = Helpers.createMockUser();
        Role adminRole = Helpers.createMockAdminRole();

        when(roleService.getRoleByType(RoleType.ADMIN)).thenReturn(adminRole);
        when(userRepository.getUserById(mockUser.getId())).thenReturn(mockUser);

        User result = userService.promoteUserToAdmin(mockUser.getId());

        assertEquals(adminRole, result.getRole());
        verify(userRepository, times(1)).updateUser(mockUser);
    }


    @Test
    public void blockUser_should_setBlockedTrue_and_save() {
        User mockUser = Helpers.createMockUser();
        when(userRepository.getUserById(mockUser.getId())).thenReturn(mockUser);

        User result = userService.blockUser(mockUser.getId());

        assertTrue(result.isBlocked());
        verify(userRepository, times(1)).updateUser(mockUser);
    }


    @Test
    public void unblockUser_should_setBlockedFalse_and_save() {
        User mockUser = Helpers.createMockUser();
        mockUser.setBlocked(true);
        when(userRepository.getUserById(mockUser.getId())).thenReturn(mockUser);

        User result = userService.unblockUser(mockUser.getId());

        assertFalse(result.isBlocked());
        verify(userRepository, times(1)).updateUser(mockUser);
    }
}
