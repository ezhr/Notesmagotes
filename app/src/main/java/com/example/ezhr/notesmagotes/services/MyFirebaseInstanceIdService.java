package com.example.ezhr.notesmagotes.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.ezhr.notesmagotes.models.Result;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ezhr.notesmagotes.UserActivity.TAG;
import static com.example.ezhr.notesmagotes.UserActivity.TOKEN_FILE;
import static com.example.ezhr.notesmagotes.UserActivity.TOKEN_KEY;
import static com.example.ezhr.notesmagotes.UserActivity.api;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    String token;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPrefs = getSharedPreferences(TOKEN_FILE, MODE_PRIVATE);
        token = sharedPrefs.getString(TOKEN_KEY, null);
    }

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "onTokenRefresh: " + fcmToken);

        if (token != null) {
            Call<Result> fcmCall = api.refreshFCMToken(token, fcmToken);
            Log.i(TAG, "onTokenRefresh: " + fcmToken);
            fcmCall.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.code() == 200) {
                        Log.i(TAG, "onResponse: " + response.body().getMessage());
                    } else {
                        Log.e(TAG, "onResponse: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.toString());
                }
            });
        }
    }
}
