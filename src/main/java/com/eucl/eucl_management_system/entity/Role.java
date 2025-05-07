package com.eucl.eucl_management_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Erole name;

    public Role() {
    }
    public Role(Erole name) {
        this.name = name;
    }
    public Role(Long id, Erole name) {
        this.id = id;
        this.name = name;
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Erole getName() {return name;}
    public void setName(Erole name) {this.name = name;}

}

