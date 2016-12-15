package com.example.ezhr.notesmagotes.models;

import java.util.UUID;

/**
 * Created by EzhR on 12/13/2016.
 */

public class User {

    String _id;
    String username;
    String email;
    String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return _id;
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
