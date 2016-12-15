package com.example.ezhr.notesmagotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezhr.notesmagotes.api.mobileAPI;
import com.example.ezhr.notesmagotes.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity {

    public final static String BASE_URL = "http://10.0.2.2:3000/api/";
    private static final String TAG = "EzhRLog";

    @BindView(R.id.userUsernameEditText)
    EditText usernameEditText;
    @BindView(R.id.userPasswordEditText)
    EditText passwordEditText;
    @BindView(R.id.userEmailEditText)
    EditText emailEditText;
    @BindView(R.id.userIdEditText)
    EditText idEditText;
    @BindView(R.id.userIndexEditText)
    EditText indexEditText;

    @BindView(R.id.indexUsersButton)
    Button indexUsersButton;
    @BindView(R.id.idUserButton)
    Button idUserButton;
    @BindView(R.id.usernameUserButton)
    Button usernameUserButton;
    @BindView(R.id.newUserButton)
    Button newUserButton;
    @BindView(R.id.switchToNotesButton)
    Button switchToNotesButton;

    Retrofit retrofit;
    mobileAPI api;

    static String username = "";
    static String password = "";
    static String email = "";
    static String id = "5851d081eac39226881595d2";
    static int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);

        Gson gson = new GsonBuilder().setLenient().create();

        // Set defaults
        idEditText.setText(id);
        indexEditText.setText(String.valueOf(index));

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(mobileAPI.class);

        indexUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexEditText.getText().toString().equals("") || indexEditText == null)
                    Toast.makeText(v.getContext(), "Please enter an index to fetch user", Toast.LENGTH_SHORT).show();
                else {
                    index = Integer.valueOf(indexEditText.getText().toString());
                    getUserByIndex();
                }
            }
        });

        idUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idEditText.getText().toString().equals("") || idEditText == null)
                    Toast.makeText(v.getContext(), "Please enter a user ID", Toast.LENGTH_SHORT).show();
                else {
                    id = idEditText.getText().toString();
                    getUserById();
                }
            }
        });

        usernameUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEditText.getText().toString().equals("") || usernameEditText == null)
                    Toast.makeText(v.getContext(), "Please enter a valid username", Toast.LENGTH_SHORT).show();
                else {
                    username = usernameEditText.getText().toString();
                    getUserByUserName();
                }
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (username.length() < 1 || username == null) {
                    Toast.makeText(UserActivity.this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
                } else if (email.length() < 1 || email == null || !email.contains("@")) {
                    Toast.makeText(UserActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 1 || password == null) {
                    Toast.makeText(UserActivity.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                } else {
                    makeNewUser();
                }
            }
        });

        switchToNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, NotesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //Finds User by Index in Database
    private void getUserByIndex() {

        Call<List<User>> call = api.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                if (index > users.size()) {
                    Toast.makeText(getBaseContext(), "Index exceeds range", Toast.LENGTH_SHORT).show();
                } else {
                    updateFields(users.get(index));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getUserById() {

        Call<User> call = api.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                updateFields(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(UserActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getUserByUserName() {
        Call<User> call = api.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                updateFields(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(UserActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFields(User user) {
        if (user == null) {
            Toast.makeText(this, "Null user returned", Toast.LENGTH_SHORT).show();
            return;
        }
        usernameEditText.setText(user.getUsername());
        passwordEditText.setText(user.getPassword());
        emailEditText.setText(user.getEmail());
        idEditText.setText(user.getId());
    }

    private void makeNewUser() {
        User user = new User(username, email, password);
        Call<User> call = api.newUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201) {
                    Toast.makeText(UserActivity.this, "User created: " + username, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
