package com.example.ezhr.notesmagotes.api;

import com.example.ezhr.notesmagotes.models.Note;
import com.example.ezhr.notesmagotes.models.Result;
import com.example.ezhr.notesmagotes.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface mobileAPI {

    // User requests
    @POST("users/new")
    Call<Result> newUser(@Body User user);

    @POST("users/authenticate")
    Call<Result> login(@Body User user);


    // Note requests
    @Headers({"Content-Type: application/json"})
    @GET("notes/all")
    Call<List<Note>> getNotes(@Header("x-token") String token);

    @Headers({"Content-Type: application/json"})
    @POST("notes/new")
    Call<Result> newNote(@Header("x-token") String token, @Body Note note);

    @Headers({"Content-Type: application/json"})
    @POST("notes/update")
    Call<Result> updateNote(@Header("x-token") String token, @Body Note note);

    @Headers({"Content-Type: application/json"})
    @GET("notes/delete")
    Call<Result> deleteNote(@Header("x-token") String token, @Header("x-id") String id);

    @Headers({"Content-Type: application/json"})
    @GET("notes/find")
    Call<List<Note>> findNotes(@Header("x-token") String token, @Header("x-search") String search);
}
