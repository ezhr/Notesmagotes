package com.example.ezhr.notesmagotes;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ezhr.notesmagotes.models.Note;

import java.util.List;

/**
 * Created by EzhR on 12/17/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NotesHolder> {

    private static List<Note> notesList;

    public static final String SELECTED_NOTE = "com.example.ezhr.notesmagotes.selectednote";

    public RecyclerAdapter(List notesList) {
        this.notesList = notesList;
    }

    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new NotesHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesHolder holder, int position) {
        holder.listTitleTextView.setText(notesList.get(position).getTitle());
        holder.listDateTextView.setText(notesList.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NotesHolder extends RecyclerView.ViewHolder {

        TextView listTitleTextView;
        TextView listDateTextView;

        public NotesHolder(final View itemView) {
            super(itemView);

            listTitleTextView = (TextView) itemView.findViewById(R.id.listTitleTextView);
            listDateTextView = (TextView) itemView.findViewById(R.id.listDateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note note = new Note(
                            notesList.get(getAdapterPosition()).getTitle(),
                            notesList.get(getAdapterPosition()).getContent(),
                            notesList.get(getAdapterPosition()).getDate(),
                            notesList.get(getAdapterPosition()).getId()
                    );
                    Intent intent = new Intent(v.getContext(), SingleNoteActivity.class);
                    intent.putExtra(SELECTED_NOTE, note);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
