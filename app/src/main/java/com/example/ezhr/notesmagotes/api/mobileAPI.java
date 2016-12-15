package com.example.ezhr.notesmagotes.api;

import com.example.ezhr.notesmagotes.models.Note;
import com.example.ezhr.notesmagotes.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface mobileAPI {

    // User requests
    @GET("users/all")
    Call<List<User>> getUsers();

    @GET("users/id/{id}")
    Call<User> getUserById(@Path("id") String id);

    @GET("users/username/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @POST("users/new")
    Call<User> newUser(@Body User user);

    // Note requests
    @GET("notes/id/{userId}")
    Call<List<Note>> getNotesById(@Path("userId") String userId);

    @GET("notes/username/{username}")
    Call<List<Note>> getNotesByUsername(@Path("username") String username);

    @POST("notes/new")
    Call<Note> saveNote(@Body Note note);
}
