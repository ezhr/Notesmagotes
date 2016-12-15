package com.example.ezhr.notesmagotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezhr.notesmagotes.api.mobileAPI;
import com.example.ezhr.notesmagotes.models.Note;
import com.example.ezhr.notesmagotes.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.ezhr.notesmagotes.api.mobileAPI;

import java.util.List;

import static com.example.ezhr.notesmagotes.UserActivity.BASE_URL;

public class NotesActivity extends AppCompatActivity {

    @BindView(R.id.noteUsernameEditText)
    EditText noteUsernameEditText;
    @BindView(R.id.noteUserIdEditText)
    EditText noteUserIdEditText;
    @BindView(R.id.noteIndexEditText)
    EditText noteIndexEditText;
    @BindView(R.id.noteTitleEditText)
    EditText noteTitleEditText;
    @BindView(R.id.noteNoteEditText)
    EditText noteNoteEditText;

    @BindView(R.id.noteIdSearchButton)
    Button noteIdSearchButton;
    @BindView(R.id.noteUsernameSearchButton)
    Button noteUsernameSearchButton;
    @BindView(R.id.noteSaveButton)
    Button noteSaveButton;
    @BindView(R.id.switchToUsersButton)
    Button switchToUsersButton;

    static int index;
    static String username;
    static String id;

    String defaultId = "5851d081eac39226881595d2";
    int defaultIndex = 0;

    mobileAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ButterKnife.bind(this);

        noteUserIdEditText.setText(defaultId);
        noteIndexEditText.setText(String.valueOf(defaultIndex));

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();

        api = retrofit.create(mobileAPI.class);

        noteIdSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteUserIdEditText.getText().length() < 1 || noteUserIdEditText == null
                        || noteIndexEditText.getText().length() < 1 || noteIndexEditText == null) {
                    Toast.makeText(NotesActivity.this, "User ID/index incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    index = Integer.valueOf(noteIndexEditText.getText().toString());
                    id = noteUserIdEditText.getText().toString();
                    searchById();
                }
            }
        });

        noteUsernameSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteUsernameEditText.getText().length() < 1 || noteUsernameEditText == null
                        || noteIndexEditText.getText().length() < 1 || noteIndexEditText == null) {
                    Toast.makeText(NotesActivity.this, "Username/index incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    index = Integer.valueOf(noteIndexEditText.getText().toString());
                    username = noteUsernameEditText.getText().toString();
                    searchByUsername();
                }
            }
        });

        noteSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteUserIdEditText.length() < 1 || noteUserIdEditText == null ||
                        noteTitleEditText.getText().length() < 1 || noteTitleEditText == null) {
                    Toast.makeText(NotesActivity.this, "Please input user ID/title", Toast.LENGTH_SHORT).show();
                } else {
                    id = noteUserIdEditText.getText().toString();
                    saveNote();
                }
            }
        });

        switchToUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void searchById() {
        Call<List<Note>> call = api.getNotesById(id);
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                Note note = response.body().get(index);
                updateFields(note);
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(NotesActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchByUsername() {
        Call<List<Note>> call = api.getNotesByUsername(username);
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                Note note = response.body().get(index);
                updateFields(note);
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(NotesActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateFields(Note note) {
        noteUsernameEditText.setText(note.getUsername());
        noteUserIdEditText.setText(note.getUserId());
        noteTitleEditText.setText(note.getTitle());
        noteNoteEditText.setText(note.getNote());
    }

    public void saveNote() {
        String title = noteTitleEditText.getText().toString();
        String noteContent = noteNoteEditText.getText().toString();

        Note note = new Note(title, noteContent, id);
        
        Call<Note> call = api.saveNote(note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.code() == 201) {
                    Toast.makeText(NotesActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(NotesActivity.this, "FAIL: Invalid user ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(NotesActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
