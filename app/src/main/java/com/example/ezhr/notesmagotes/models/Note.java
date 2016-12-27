package com.example.ezhr.notesmagotes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by EzhR on 12/15/2016.
 */

public class Note implements Parcelable {

    String title;
    String content;
    Date date;
    String _id;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Note() {
        _id = null;
    }

    public Note(String title, String content, Date date, String id) {
        this.title = title;
        this.content = content;
        this.date = date;
        this._id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return _id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(date.getTime());
        dest.writeString(_id);
    }

    private Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        date = new Date(in.readLong());
        _id = in.readString();
    }
}
