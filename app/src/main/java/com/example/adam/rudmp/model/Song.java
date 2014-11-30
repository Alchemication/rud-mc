package com.example.adam.rudmp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adam on 29/11/14.
 *
 * We need to implement parcelable
 * to be able to easily save/retrieve
 * a arrayList of objects
 */
public class Song implements Parcelable {

    private String artist;
    private String title;
    private String link;

    public Song(String artist, String title, String link) {
        this.artist = artist;
        this.title  = title;
        this.link   = link;
    }

    private Song(Parcel in) {
        artist = in.readString();
        title  = in.readString();
        link   = in.readString();
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString() {
        return getArtist() + ": " + getTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(artist);
        parcel.writeString(title);
        parcel.writeString(link);
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
