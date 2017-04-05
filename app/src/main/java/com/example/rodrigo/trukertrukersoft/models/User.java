package com.example.rodrigo.trukertrukersoft.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Rodrigo on 28/03/2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    private int UserId;
    @Required
    private String Username;
    @Required
    private String Password;

    public User() {
    }

    public User(int userId, String username, String password) {
        UserId = userId;
        Username = username;
        Password = password;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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