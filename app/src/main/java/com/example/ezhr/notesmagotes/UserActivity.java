package com.example.ezhr.notesmagotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezhr.notesmagotes.api.mobileAPI;
import com.example.ezhr.notesmagotes.models.Result;
import com.example.ezhr.notesmagotes.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.loginTextView)
    TextView loginTextView;
    @BindView(R.id.signupTextView)
    TextView signupTextView;
    @BindView(R.id.usernameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.userGoTextView)
    TextView userGoTextView;
    @BindView(R.id.serverUrlEditText)
    EditText serverUrlEditText;


    public static String BASE_URL = null;
    public final static String TOKEN_FILE = "com.example.ezhr.notesmagotes.token";
    public final static String TOKEN_KEY = "com.example.ezhr.notesmagotes.tokenkey";
    public final static String USER_NAME = "com.example.ezhr.notesmagotes.username";

    public final static String SERVER_FILE = "com.example.ezhr.notesmagotes.server";
    public final static String SERVER_KEY = "com.example.ezhr.notesmagotes.serverkey";

    public static final String TAG = "notesmagotes.log";

    public static mobileAPI api;

    boolean loginCondition = true;

    SharedPreferences tokenSharedPrefs;
    SharedPreferences serverIpSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String abc = null;
        Log.e(TAG, "onCreate: " + abc);
        abc = "abc";
        Log.e(TAG, "onCreate: " + abc);


        tokenSharedPrefs = getSharedPreferences(TOKEN_FILE, MODE_PRIVATE);
        serverIpSharedPrefs = getSharedPreferences(SERVER_FILE, MODE_PRIVATE);

        if (tokenSharedPrefs.getString(TOKEN_KEY, null) != null
                && tokenSharedPrefs.getString(USER_NAME, null) != null
                && serverIpSharedPrefs.getString(SERVER_KEY, null) != null)
            goToNotes();

        ButterKnife.bind(this);

        if (serverIpSharedPrefs.getString(SERVER_KEY, null) == null) {
            serverUrlEditText.setText("10.0.2.2:3000/");
        } else {
            serverUrlEditText.setText(serverIpSharedPrefs.getString(SERVER_KEY, null));
        }

        loginTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        //usernameEditText.setText("beth");
        //passwordEditText.setText("DEVc4xaeJfvvnFJCZZ");

        usernameEditText.requestFocus();

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = (TextView) v;
                view.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
                signupTextView.setTextColor(ContextCompat.getColor(v.getContext(), android.R.color.tertiary_text_dark));
                loginCondition = true;
                Log.i(TAG, "onClick: " + loginCondition);
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = (TextView) v;
                view.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
                loginTextView.setTextColor(ContextCompat.getColor(v.getContext(), android.R.color.tertiary_text_dark));
                loginCondition = false;
                Log.i(TAG, "onClick: " + loginCondition);
            }
        });

        userGoTextView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              String enteredUrl = serverUrlEditText.getText().toString();
                                              Log.i(TAG, "setupApi: " + enteredUrl);
                                              if (enteredUrl.charAt(enteredUrl.length() - 1) != '/')
                                                  enteredUrl = enteredUrl + "/";
                                              serverIpSharedPrefs.edit().putString(SERVER_KEY, enteredUrl).commit();

                                              setupApi();

                                              if (usernameEditText.getText().length() < 1) {
                                                  Toast.makeText(UserActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                                              } else if (passwordEditText.getText().length() < 1) {
                                                  Toast.makeText(UserActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                                              } else {
                                                  final String username = usernameEditText.getText().toString();
                                                  final String password = passwordEditText.getText().toString();
                                                  if (loginCondition) {
                                                      login(username, password);
                                                  } else {
                                                      signup(username, password);
                                                  }
                                              }
                                          }
                                      }
        );
    }

    private void login(final String username, final String password) {
        User user = new User(username, password);
        Call<Result> call = api.login(user);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 400) {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Toast.makeText(UserActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e);
                    }
                } else {
                    SharedPreferences.Editor editor = tokenSharedPrefs.edit();
                    editor.putString(TOKEN_KEY, response.body().getToken()).commit();
                    editor.putString(USER_NAME, username).commit();
                    goToNotes();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }


    private void signup(final String username, final String password) {
        User user = new User(username, password);
        Call<Result> call = api.newUser(user);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 409 || response.code() == 400) {
                    JSONObject object;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        Toast.makeText(UserActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e);
                    }
                } else if (response.code() == 200) {
                    login(username, password);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    private void goToNotes() {
        setupApi();
        startActivity(new Intent(UserActivity.this, NotesActivity.class));
        finish();
    }

    private void setupApi() {

        BASE_URL = "http://" + serverIpSharedPrefs.getString(SERVER_KEY, null) + "api/";

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();
        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        api = retrofit.create(mobileAPI.class);
    }
}
