package net.javaguides.springboot.controller;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Auth {
    private String username;
    private String password;

    @JsonCreator
    public Auth() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
