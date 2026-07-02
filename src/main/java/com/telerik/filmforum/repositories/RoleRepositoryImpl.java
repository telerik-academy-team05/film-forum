package com.telerik.filmforum.repositories;

import com.telerik.filmforum.exceptions.EntityNotFoundException;
import com.telerik.filmforum.models.Role;
import com.telerik.filmforum.models.RoleType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Role getRoleById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Role role = session.find(Role.class, id);
            if (role == null) {
                throw new EntityNotFoundException("Role", id);
            }
            return role;
        }
    }

    @Override
    public Role getRoleByType(RoleType roleType) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery(
                    "from Role where roleType = :roleType", Role.class);
            query.setParameter("roleType", roleType);
            List<Role> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Role", "type", roleType.name());
            }
            return result.get(0);
        }
    }

    @Override
    public List<Role> getAllRoles() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Role", Role.class).list();
        }
    }
}
