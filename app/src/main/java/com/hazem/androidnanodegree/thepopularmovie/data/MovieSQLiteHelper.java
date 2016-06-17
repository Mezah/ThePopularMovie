package com.hazem.androidnanodegree.thepopularmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mezah on 5/31/2016.
 */
public class MovieSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=4;
    private static final String DATABASE_NAME ="movies.db";

    public MovieSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MovieContract.MovieQuery.CREATE_MOVIE_TABLE);
        db.execSQL(MovieContract.MovieQuery.CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+MovieContract.MovieTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MovieContract.FavoriteTable.TABLE_NAME);
        onCreate(db);
    }
}
