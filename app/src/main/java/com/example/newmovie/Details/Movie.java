package com.example.newmovie.Details;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Parcelable, Serializable {
    String original_title;
    String poster_path;
    String release_date;
    String overview;
    long id;
    double vote_average;
    public Movie(){};

    protected Movie(Parcel in) {
        original_title = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
        id=in.readLong();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(overview);
        dest.writeDouble(vote_average);
        dest.writeLong(id);
    }
}
