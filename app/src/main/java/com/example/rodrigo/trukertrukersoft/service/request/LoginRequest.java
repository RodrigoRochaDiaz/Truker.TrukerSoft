package com.example.rodrigo.trukertrukersoft.service.request;



/**
 * Created by Rodrigo on 29/03/2017.
 */

public class LoginRequest {
    private String Username;
    private String Password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        Username = username;
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

