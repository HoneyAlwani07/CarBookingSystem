package com.carbooking.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String cnic;
    private String email;
    private String role;

    public User() {}

    public User(String username, String password, String cnic, String email, String role) {
        this.username = username;
        this.password = password;
        this.cnic = cnic;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCnic() { return cnic; }
    public void setCnic(String cnic) { this.cnic = cnic; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
