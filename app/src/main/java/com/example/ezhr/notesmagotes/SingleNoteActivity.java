package com.example.ezhr.notesmagotes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Note");

        noteId = "";

        ButterKnife.bind(this);
        note = getIntent().getParcelableExtra(SELECTED_NOTE);

        if (note != null) {
            noteId = note.getId();
            singleTitleEditText.setText(note.getTitle());
            singleContentEditText.setText(note.getContent());
        }

        final String originalTitle = singleTitleEditText.getText().toString();
        final String originalContent = singleContentEditText.getText().toString();


        singleSaveTextView.setEnabled(false);
        singleSaveTextView.setTextColor(ContextCompat.getColor(SingleNoteActivity.this, android.R.color.tertiary_text_light));
        singleTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(originalTitle)) {
                    singleSaveTextView.setEnabled(false);
                    singleSaveTextView.setTextColor(ContextCompat.getColor(SingleNoteActivity.this, android.R.color.tertiary_text_light));
                    singleDiscardTextView.setText("CANCEL");
                    edited = false;
                } else {
                    singleSaveTextView.setEnabled(true);
                    singleSaveTextView.setTextColor(ContextCompat.getColor(SingleNoteActivity.this, R.color.colorAccent));
                    edited = true;
                    singleDiscardTextView.setText("DISCARD");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        singleContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(originalContent)) {
                    singleSaveTextView.setEnabled(false);
                    singleSaveTextView.setTextColor(ContextCompat.getColor(SingleNoteActivity.this, android.R.color.tertiary_text_light));
                    edited = false;
                    singleDiscardTextView.setText("CANCEL");
                } else {
                    singleSaveTextView.setEnabled(true);
                    singleSaveTextView.setTextColor(ContextCompat.getColor(SingleNoteActivity.this, R.color.colorAccent));
                    edited = true;
                    singleDiscardTextView.setText("DISCARD");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        singleDiscardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edited)
                    discardNote();
            }
        });

        singleSaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = singleTitleEditText.getText().toString();
                String content = singleContentEditText.getText().toString();
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

    private void discardNote() {
        if (edited) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Discard Note")
                    .setMessage("Are you sure you want to discard this note? (Changes made will not be saved)")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
        } else
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.singlenote, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.deleteNote):
                if (note == null || note.getId() == null) {
                    if (edited)
                        discardNote();
                    else {
                        finish();
                        return true;
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SingleNoteActivity.this);

                    builder.setTitle("Delete Note")
                            .setMessage("Are you sure you want to PERMANENTLY delete this note?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<Result> call = api.deleteNote(token, note);
                                    call.enqueue(new Callback<Result>() {
                                        @Override
                                        public void onResponse(Call<Result> call, Response<Result> response) {
                                            if (response.code() == 200) {
                                                Toast.makeText(SingleNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(SingleNoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Result> call, Throwable t) {
                                            Log.e(TAG, "onFailure: " + t.toString());
                                        }
                                    });
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                }

            case (android.R.id.home):
                discardNote();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        discardNote();
    }
}