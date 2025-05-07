package com.eucl.eucl_management_system.dto.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type="Bearer";
    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<String> roles;

    public JwtResponse(String token, Long id, String name, String email, String phone, List<String> roles) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
    }
    public String getToken() {return token;}
    public String getType() {return type;}
    public Long getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public List<String> getRoles() {return roles;}
    public void setToken(String token) {this.token = token;}
    public void setType(String type) {this.type = type;}
    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setRoles(List<String> roles) {this.roles = roles;}
}
