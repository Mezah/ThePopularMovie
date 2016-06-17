package com.hazem.androidnanodegree.thepopularmovie.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Mezah on 5/31/2016.
 */
public class MovieContentProvider extends ContentProvider {


    private MovieSQLiteHelper movieHelper;
    private static final UriMatcher sUriMatcher = createMatcher();
    private static final SQLiteQueryBuilder favoriteMovieBuilder;
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 110;
    public static final int FAVORITES = 200;
    public static final int FAVORITE_WITH_ID=210;


    // initialize joining favorite and movie tables
    static {
        favoriteMovieBuilder=new SQLiteQueryBuilder();

        favoriteMovieBuilder.setTables(
                        MovieContract.MovieTable.TABLE_NAME
                        +" INNER JOIN "+
                        MovieContract.FavoriteTable.TABLE_NAME+
                        " ON "
                        +MovieContract.MovieTable.TABLE_NAME+"."+ MovieContract.MovieTable.COL_MOVIE_ID+
                        "="+
                        MovieContract.FavoriteTable.TABLE_NAME+"."+ MovieContract.FavoriteTable.COL_FAVORITE_MOVIE_ID);
    }
    @Override
    public boolean onCreate() {
        movieHelper = new MovieSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:

                return MovieContract.MovieTable.TABLE_CONTENT_TYPE;

            case MOVIES_WITH_ID:
                return MovieContract.MovieTable.TABLE_ITEM_CONTENT_TYPE;

            case FAVORITES:
                return MovieContract.FavoriteTable.TABLE_CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = movieHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match) {
            case MOVIES: {
                returnCursor = db.query(
                        MovieContract.MovieTable.TABLE_NAME
                        , projection,
                        selection,
                        selectionArgs,
                        null,
                        null, sortOrder);
                break;
            }

            case MOVIES_WITH_ID: {
                selection = MovieContract.MovieTable.COL_MOVIE_ID + "=?";
                final String id = MovieContract.getIdFromUri(uri);
                Log.d("MOVIE_URI", id);
                selectionArgs = new String[]{id};
                returnCursor = db.query(
                        MovieContract.MovieTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;

            }

            case FAVORITES: {
                returnCursor = db.query(
                        MovieContract.FavoriteTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FAVORITE_WITH_ID:{

                returnCursor=favoriteMovieBuilder.query(
                        db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = movieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {

            case MOVIES: {
                long id = db.insert(MovieContract.MovieTable.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.MovieTable.buildMovieUri(id);
                } else {

                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case FAVORITES:{
                long id = db.insert(MovieContract.FavoriteTable.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.FavoriteTable.buildMovieUri(id);
                } else {

                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Notify the content resolver that a change occurs
        getContext().getContentResolver().notifyChange(uri, null);
        // close the database
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowId;
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES: {
                rowId = db.delete(MovieContract.MovieTable.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FAVORITES:{
                rowId = db.delete(MovieContract.FavoriteTable.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (rowId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowId;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = movieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowId;

        switch (match) {
            case MOVIES: {

                rowId = db.update(MovieContract.MovieTable.TABLE_NAME, values, selection, selectionArgs);

                break;
            }

            case MOVIES_WITH_ID: {
                rowId = db.update(MovieContract.MovieTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            case FAVORITES: {

                rowId = db.update(MovieContract.FavoriteTable.TABLE_NAME, values, selection, selectionArgs);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (rowId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        db.close();
        return rowId;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = movieHelper.getWritableDatabase();

        switch (match) {
            case MOVIES: {
                db.beginTransaction();
                int itemCount = 0;
                try {
                    for (ContentValues contentValue : values) {

                        long id = db.insert(MovieContract.MovieTable.TABLE_NAME, null, contentValue);

                        // check insertion state
                        if (id != -1) {
                            // success
                            itemCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {

                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return itemCount;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static UriMatcher createMatcher() {
        // default that there is no match
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/*", MOVIES_WITH_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES+"/*", FAVORITE_WITH_ID);
        return uriMatcher;
    }


}
