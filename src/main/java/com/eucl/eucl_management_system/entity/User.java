package com.eucl.eucl_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "nationalId"),
        @UniqueConstraint(columnNames = "phone")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Username is required")
    @Size(max = 100)
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid having @")
    @Size(max = 100)
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number shoulw be 10 - 15 digits")
    private String phone;
    @NotBlank(message = "National ID is required")
    @Size(min = 16, max = 16, message = "National ID shouls be 16 digits")
    private String nationalId;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 10, message = "Password should be at least 8 characters")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    public User(){}
    public User(String name, String email, String phone, String nationalId, String password){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nationalId = nationalId;
        this.password = password;
    }
    public  Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public  void setName(String name){this.name=name;}
    public String getEmail(){return  email;}
    public void setEmail(String email){this.email=email;}
    public String getPhone(){return  phone;}
    public void setPhone(String phone){this.phone=phone;}
    public String getNationalId(){return  nationalId;}
    public void setNationalId(String nationalId){this.nationalId=nationalId;}
    public String getPassword(){return  password;}
    public void setPassword(String password){this.password=password;}
    public Set<Role> getRoles(){return roles;}
    public void setRoles(Set<Role> roles){this.roles=roles;}

}
