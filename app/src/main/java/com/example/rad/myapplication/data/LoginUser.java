package com.example.rad.myapplication.data;


public class LoginUser {


    private String username;

    private String email;

    private String password;

    public static LoginUser regular(String username, String password) {
        return new LoginUser(username, password);
    }

    private LoginUser(String username, String password) {
        this.username = username;
        this.email = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
