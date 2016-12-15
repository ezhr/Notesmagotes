package com.example.ezhr.notesmagotes.models;

/**
 * Created by EzhR on 12/15/2016.
 */

public class Note {

    String title;
    String note;
    String userId;
    String username;

    public Note(String title, String note, String userId) {
        this.title = title;
        this.note = note;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
