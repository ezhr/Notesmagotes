package com.example.ezhr.notesmagotes.models;

/**
 * Created by EzhR on 12/13/2016.
 */

public class User {

    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
}
