package com.telerik.filmforum.services;

import com.telerik.filmforum.models.Role;
import com.telerik.filmforum.models.RoleType;

import java.util.List;

public interface RoleService {

    Role getRoleById(int id);

    Role getRoleByType(RoleType roleType);

    List<Role> getAllRoles();
}
