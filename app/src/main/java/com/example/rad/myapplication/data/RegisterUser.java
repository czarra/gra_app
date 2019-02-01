package com.example.rad.myapplication.data;


public class RegisterUser {


    private String username;

    private String email;

    private String password;


    public static RegisterUser regular(String username, String email, String password) {
        return new RegisterUser(username, email, password);
    }

    private RegisterUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
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
