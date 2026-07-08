package com.telerik.filmforum.helpers;

import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.models.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {

    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";

    public void checkAccessPermissions (int targetUserId, User executingUser){
        if (!executingUser.isAdmin() && targetUserId != executingUser.getId()){
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    public void isAdmin (User executingUser){
        if (!executingUser.isAdmin()){
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
