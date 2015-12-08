package com.example.android.popularmoviestest;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * Created by ToM on 16/11/2015.
 */
public class Movie implements Parcelable {

    public String title;
    public String releaseDate;
    public String overview;
    public String posterPath;
    public String vote;
    public String popularity;

    public Movie(String title, String releaseDate, String overview, String posterPath, String vote,String popularity)
    {
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.posterPath = posterPath;
        this.vote = vote;
        this.popularity = popularity;
    }

    private Movie(Parcel in)
    {
        title = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        vote = in.readString();
        popularity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {return  title + releaseDate + overview + posterPath + vote + popularity;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(vote);
        parcel.writeString(popularity);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
