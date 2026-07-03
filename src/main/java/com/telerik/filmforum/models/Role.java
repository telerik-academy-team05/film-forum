package com.telerik.filmforum.models;

import jakarta.persistence.*;

@Entity
@Table (name = "roles")
public class Role {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "role_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column (name = "role")
    private RoleType roleType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
