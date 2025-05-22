package com.example.roomreservation.db.models;

public class User {
    private String email;
    private String password;

    // Constructor
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter dan Setter untuk email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter dan Setter untuk password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}