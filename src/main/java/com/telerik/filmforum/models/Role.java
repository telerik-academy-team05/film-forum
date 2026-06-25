package com.telerik.filmforum.models;

import jakarta.persistence.*;

@Entity
@Table (name = "roles")
public class Role {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "role_id")
    private int id;

    @Column (name = "role")
    private String roleName;
}
