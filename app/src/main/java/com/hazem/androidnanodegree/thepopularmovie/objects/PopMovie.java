package com.hazem.androidnanodegree.thepopularmovie.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mezah on 5/7/2016.
 */
public class PopMovie implements Parcelable {
    // movie url
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w300";
    public static final String DIRECTORY="pop_movie";
    public static final String DIR="/data/data/com.hazem.androidnanodegree.thepopularmovie/";
    public static final String EXT=".jpg";
    // movie details
    private String id;
    private String posterName;       // "\/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg"
    private String backdropName;     // "\/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg"
    private String original_title;
    private double vote_average;
    private String year;
    private String overview;

    public PopMovie(String id, String posterName, String backdropName, String title, double rating, String year, String plot) {
        this.id = id;
        this.posterName = posterName;
        this.backdropName = backdropName;
        this.original_title = title;
        this.vote_average = rating;
        this.year = year;
        this.overview = plot;
    }

    // private constructor needed for the object to parcelable
    private PopMovie(Parcel in) {
        id = in.readString();
        posterName = in.readString();
        backdropName = in.readString();
        original_title = in.readString();
        vote_average = in.readDouble();
        year = in.readString();
        overview = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getPosterName(){

        return posterName;
    }

    public String getPosterUrl() {

        // http://image.tmdb.org/t/p/w300/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg
        return BASE_URL +"/"+ posterName+EXT;
    }

    public String getBackdropName(){

        return backdropName; // sM33SANp9z6rXW8Itn7NnG1GOEs
    }

    public String getBackdropUrl() {

        // http://image.tmdb.org/t/p/w300/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg
        return BASE_URL +"/"+ backdropName+EXT;
    }

    public String getTitle() {
        return original_title;
    }

    public double getRate(){
        return vote_average;
    }
    public String getAverageRate() {

        return ((int) vote_average) + "/10";
    }

    public String getYear() {

        return year;
    }

    public String getPlot() {
        return overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // the information that is required to moved with the movie object in case of using
        // it as parcelable object

        dest.writeString(id);
        dest.writeString(posterName);
        dest.writeString(backdropName);
        dest.writeString(original_title);
        dest.writeDouble(vote_average);
        dest.writeString(year);
        dest.writeString(overview);

    }

    public static final Parcelable.Creator<PopMovie> CREATOR
            = new Parcelable.Creator<PopMovie>() {
        public PopMovie createFromParcel(Parcel in) {
            return new PopMovie(in);
        }

        public PopMovie[] newArray(int size) {
            return new PopMovie[size];
        }
    };


    @Override
    public String toString() {
        // overriding the toString() to return friendly message with movie information
        return getId() + "\n" + getPosterUrl() + "\n" + getTitle() + "\n" + getYear() + "\n" + getPlot() + "\n" + getAverageRate()+ "\n"+getBackdropUrl();
    }
}
