package com.example.rad.myapplication.data;


public class LoginUser {


    private String username;

    private String password;

    public static LoginUser regular(String email, String password) {
        return new LoginUser(email, password);
    }

    private LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
