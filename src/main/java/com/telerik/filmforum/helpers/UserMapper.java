package com.telerik.filmforum.helpers;

import com.telerik.filmforum.models.User;
import com.telerik.filmforum.models.UserDto;
import com.telerik.filmforum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        return mapCommonFields(userDto, user);
    }

    public User fromDto(int id, UserDto dto) {
        User user = userService.getUserById(id);
        return mapCommonFields(dto, user);
    }

    private User mapCommonFields(UserDto userDto, User user) {
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setProfilePhotoUrl(userDto.getProfilePhotoUrl());

        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setProfilePhotoUrl(user.getProfilePhotoUrl());

        return userDto;
    }
}
