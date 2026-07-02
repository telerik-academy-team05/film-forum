package com.telerik.filmforum.services;

import com.telerik.filmforum.models.Role;
import com.telerik.filmforum.models.RoleType;
import com.telerik.filmforum.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {

        this.repository = repository;
    }

    @Override
    public Role getRoleById(int id) {

        return repository.getRoleById(id);
    }

    @Override
    public Role getRoleByType(RoleType roleType) {

        return repository.getRoleByType(roleType);
    }

    @Override
    public List<Role> getAllRoles() {

        return repository.getAllRoles();
    }
}
