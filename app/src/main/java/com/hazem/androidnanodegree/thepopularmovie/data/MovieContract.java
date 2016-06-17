package com.hazem.androidnanodegree.thepopularmovie.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mezah on 5/31/2016.
 */
public class MovieContract {

    // This is the name of the entire content provider
    public static final String CONTENT_AUTHORITY = "com.hazem.androidnanodegree.thepopularmovie";

    // this is the basic uri for the content provider that will be used to communicate
    // with content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // this path is used to make a uri for the movie table
    public static final String PATH_MOVIES = "movies";

    // this path is used to make a uri for the movie table
    public static final String PATH_FAVORITES = "favorites";


    public static class MovieTable implements BaseColumns {
        // this uri will be used to interact with table content

        // content://com.hazem.androidnanodegree.thepopularmovie/movies
        public static final Uri TABLE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // vnd.android.cursor.dir/com.hazem.androidnanodegree.thepopularmovie/movies
        public static final String TABLE_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_MOVIES;

        // vnd.android.cursor.item/com.hazem.androidnanodegree.thepopularmovie/movies
        public static final String TABLE_ITEM_CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_MOVIES;

        // column names
        public static final String TABLE_NAME = "movies";

        public static final String COL_MOVIE_ID = "id";

        public static final String COL_TITLE = "title";

        public static final String COL_OVERVIEW = "overview";

        public static final String COL_AVERAGE_VOTE = "vote";

        public static final String COL_RELEASE_DATE = "release_date";

        public static final String COL_POSTER_NAME = "poster_name";

        public static final String COL_POSTER_DIR = "poster_dir";

        public static final String COL_BACKDROP_NAME = "backdrop_name";

        public static final String COL_BACKDROP_DIR = "backdrop_dir";

        public static final String COL_MOVIE_CATEGORY="category";


        public static final String[] TABLE_COLUMNS = {
                MovieTable.TABLE_NAME + "." + MovieTable._ID,
                MovieTable.TABLE_NAME + "." + COL_MOVIE_ID,
                COL_TITLE,
                COL_OVERVIEW,
                COL_AVERAGE_VOTE,
                COL_RELEASE_DATE,
                COL_POSTER_NAME,
                COL_POSTER_DIR,
                COL_BACKDROP_NAME,
                COL_BACKDROP_DIR,
                COL_MOVIE_CATEGORY
        };

        // column ids

        public static final int INDEX_MOVIE_ID = 1;

        public static final int INDEX_MOVIE_TITLE = 2;

        public static final int INDEX_MOVIE_OVERVIEW = 3;

        public static final int INDEX_MOVIE_AVERAGE_VOTE = 4;

        public static final int INDEX_MOVIE_RELEASE_DATE = 5;

        public static final int INDEX_MOVIE_POSTER_NAME = 6;

        public static final int INDEX_MOVIE_POSTER_DIR = 7;

        public static final int INDEX_MOVIE_BACKDROP_NAME = 8;

        public static final int INDEX_MOVIE_BACKDROP_DIR = 9;

        public static final int INDEX_MOVIE_CATEGORY=10;


        public static Uri buildMovieUriWithRate(double rate) {
            return TABLE_CONTENT_URI.buildUpon().appendPath(Double.toString(rate)).build();
        }

        // build a uri with an id appended to it.
        // this will be used to make a uri of an insertion in the table
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(TABLE_CONTENT_URI, id);
        }

        public static Uri buildMovieWithId(String movieId){
            return TABLE_CONTENT_URI.buildUpon().appendPath(movieId).build();
        }



    }

    public static class FavoriteTable implements BaseColumns{
        // this uri will be used to interact with table content

        // content://com.hazem.androidnanodegree.thepopularmovie/movies
        public static final Uri TABLE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        // vnd.android.cursor.dir/com.hazem.androidnanodegree.thepopularmovie/movies
        public static final String TABLE_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_FAVORITES;

        // column names
        public static final String TABLE_NAME = "favorites";

        public static final String COL_FAVORITE_MOVIE_ID = "favorite_id";

        public static final String COL_FAVORITE = "favorite";

        public static final String[] TABLE_COLUMNS = {
                FavoriteTable._ID,
                COL_FAVORITE_MOVIE_ID,
                COL_FAVORITE,
        };

        public static final int INDEX_MOVIE_ID = 1;

        public static final int INDEX_FAVORITE = 2;

        // build a uri with an id appended to it.
        // this will be used to make a uri of an insertion in the table
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(TABLE_CONTENT_URI, id);
        }

        public static Uri buildFavoriteWithId(String movieId){
            return TABLE_CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        public static final String[] JOINED_COLUMNS={
                MovieContract.MovieTable.TABLE_NAME + "." + MovieContract.MovieTable._ID,
                MovieContract.MovieTable.TABLE_NAME + "." + MovieContract.MovieTable.COL_MOVIE_ID,
                MovieContract.MovieTable.COL_TITLE,
                MovieContract.MovieTable.COL_AVERAGE_VOTE,
                MovieContract.MovieTable.COL_RELEASE_DATE,
                MovieContract.MovieTable.COL_POSTER_DIR
        };



    }


    public static class MovieQuery {

        public static final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieTable.TABLE_NAME + "( " +
                MovieTable._ID                  + " INTEGER PRIMARY KEY, " +
                MovieTable.COL_MOVIE_ID         + " INTEGER UNIQUE ON CONFLICT REPLACE, "      +
                MovieTable.COL_TITLE            + " TEXT NOT NULL, " +
                MovieTable.COL_OVERVIEW         + " TEXT NOT NULL, " +
                MovieTable.COL_AVERAGE_VOTE     + " REAL, "          +
                MovieTable.COL_RELEASE_DATE     + " TEXT NOT NULL, " +
                MovieTable.COL_POSTER_NAME      + " TEXT NOT NULL, " +
                MovieTable.COL_POSTER_DIR       + " TEXT NOT NULL, " +
                MovieTable.COL_BACKDROP_NAME    + " TEXT NOT NULL, " +
                MovieTable.COL_BACKDROP_DIR     + " TEXT NOT NULL, " +
                MovieTable.COL_MOVIE_CATEGORY   + " TEXT NOT NULL, "    +
                "FOREIGN KEY ("+MovieTable.COL_MOVIE_ID+")"+ " REFERENCES "+
                FavoriteTable.TABLE_NAME+" ("+FavoriteTable.COL_FAVORITE_MOVIE_ID+") "+
                ");";

        public static final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteTable.TABLE_NAME + "( " +
                FavoriteTable._ID                           + " INTEGER PRIMARY KEY, " +
                FavoriteTable.COL_FAVORITE_MOVIE_ID         + " INTEGER UNIQUE ON CONFLICT REPLACE, "      +
                FavoriteTable.COL_FAVORITE                  + " TEXT "  +
                ");";

    }


    public static String getIdFromUri(Uri uri){

        // movie/[ id ]
        return uri.getLastPathSegment();
    }



}
