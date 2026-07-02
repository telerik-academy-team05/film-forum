package com.telerik.filmforum.repositories;

import com.telerik.filmforum.models.Role;
import com.telerik.filmforum.models.RoleType;

import java.util.List;

public interface RoleRepository {

    Role getRoleById(int id);

    Role getRoleByType(RoleType roleType);

    List<Role> getAllRoles();
}
