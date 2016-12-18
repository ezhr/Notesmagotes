package com.example.ezhr.notesmagotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ezhr.notesmagotes.models.Note;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ezhr.notesmagotes.UserActivity.TAG;
import static com.example.ezhr.notesmagotes.UserActivity.TOKEN_FILE;
import static com.example.ezhr.notesmagotes.UserActivity.TOKEN_KEY;
import static com.example.ezhr.notesmagotes.UserActivity.USER_NAME;
import static com.example.ezhr.notesmagotes.UserActivity.api;

public class NotesActivity extends AppCompatActivity {

    public static String token;

    @BindView(R.id.notesList)
    RecyclerView notesRecyclerView;
    @BindView(R.id.newNoteTextView)
    TextView newNoteTextView;

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        sharedPrefs = getSharedPreferences(TOKEN_FILE, MODE_PRIVATE);
        token = sharedPrefs.getString(TOKEN_KEY, null);
        String username = sharedPrefs.getString(USER_NAME, null);
        if (token == null || username == null) {
            startActivity(new Intent(NotesActivity.this, UserActivity.class));
            finish();
        }

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(username + "'s Notes");

        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newNoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SingleNoteActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        fetchNotes();
        super.onStart();
    }

    private void fetchNotes() {
        Call<List<Note>> call = api.getNotes(token);
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.code() == 200) {
                    List<Note> notesList = new ArrayList<Note>();
                    for (Note note : response.body()) {
                        notesList.add(note);
                    }
                    notesRecyclerView.setAdapter(new RecyclerAdapter(notesList));
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.noteslist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                sharedPrefs.edit().clear().commit();
                startActivity(new Intent(NotesActivity.this, UserActivity.class));
        }
        return true;
    }
}
