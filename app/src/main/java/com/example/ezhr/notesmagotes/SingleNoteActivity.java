package com.example.ezhr.notesmagotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezhr.notesmagotes.models.Note;
import com.example.ezhr.notesmagotes.models.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ezhr.notesmagotes.NotesActivity.token;
import static com.example.ezhr.notesmagotes.RecyclerAdapter.SELECTED_NOTE;
import static com.example.ezhr.notesmagotes.UserActivity.TAG;
import static com.example.ezhr.notesmagotes.UserActivity.api;

public class SingleNoteActivity extends AppCompatActivity {

    static String noteId;

    Note note;

    @BindView(R.id.singleTitleEditText)
    EditText singleTitleEditText;
    @BindView(R.id.singleContentEditText)
    EditText singleContentEditText;
    @BindView(R.id.singleDiscardTextView)
    TextView singleDiscardTextView;
    @BindView(R.id.singleSaveTextView)
    TextView singleSaveTextView;
    @BindView(R.id.saveNoteProgressBar)
    ProgressBar saveNoteProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        noteId = "";

        ButterKnife.bind(this);

        note = getIntent().getParcelableExtra(SELECTED_NOTE);

        if (note != null) {
            noteId = note.getId();
            singleTitleEditText.setText(note.getTitle());
            singleContentEditText.setText(note.getContent());
        }

        singleDiscardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Discard Note")
                        .setMessage("Are you sure you want to discard this note? (Changes made will not be saved)")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(SingleNoteActivity.this, NotesActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        singleSaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = singleTitleEditText.getText().toString();
                String content = singleContentEditText.getText().toString();
                if (title.length() < 1 || title == null) {
                    Toast.makeText(SingleNoteActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveNoteProgressBar.setVisibility(View.VISIBLE);
                if (note == null)
                    note = new Note();
                note.setTitle(title);
                note.setContent(content);
                if (noteId != null && noteId != "") {
                    Call<Result> call = api.updateNote(token, note);
                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.code() == 200) {
                                Toast.makeText(SingleNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Log.e(TAG, "onResponse: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t.toString());
                        }
                    });
                } else {
                    Call<Result> call = api.newNote(token, note);
                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.code() == 201) {
                                Toast.makeText(SingleNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
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
        });
    }
}