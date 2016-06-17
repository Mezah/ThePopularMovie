package com.hazem.androidnanodegree.thepopularmovie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;
import com.hazem.androidnanodegree.thepopularmovie.fragments.FavoriteFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Mezah on 6/14/2016.
 */
public class FavoriteCursorAdapter extends CursorAdapter {


    private FavoriteFragment.FavoriteCallback mCallback;

    public static final int JOIN_INDEX_MOVIE_ID = 1;

    public static final int JOIN_INDEX_MOVIE_TITLE = 2;

    public static final int JOIN_INDEX_MOVIE_AVERAGE_VOTE = 3;

    public static final int JOIN_INDEX_MOVIE_RELEASE_DATE = 4;

    public static final int JOIN_INDEX_MOVIE_POSTER_DIR = 5;

    public FavoriteCursorAdapter(Context context, Cursor c, int flags, FavoriteFragment.FavoriteCallback callback) {
        super(context, c, flags);
        mCallback=callback;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_favorite_item, parent, false);

        FavoriteViewHolder holder=new FavoriteViewHolder(view);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        FavoriteViewHolder holder= (FavoriteViewHolder) view.getTag();

        String movieTitle=cursor.getString(JOIN_INDEX_MOVIE_TITLE);
        String moviePosterPath=cursor.getString(JOIN_INDEX_MOVIE_POSTER_DIR);
        String movieRate=cursor.getString(JOIN_INDEX_MOVIE_AVERAGE_VOTE);
        String movieYear=cursor.getString(JOIN_INDEX_MOVIE_RELEASE_DATE);
        final String movieId=cursor.getString(JOIN_INDEX_MOVIE_ID);

        holder.movieTitle.setText(movieTitle);
        holder.movieRate.setText(movieRate);
        holder.movieYear.setText(movieYear);
        Picasso.with(context).load(moviePosterPath).into(holder.moviePoster);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteFavoriteFromDatabase(context,movieId);

            }
        });

        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // go to movie details
                Uri movieUri= MovieContract.MovieTable.buildMovieWithId(movieId);

                mCallback.onFavoriteSelected(movieUri);
            }
        });
    }


    static class FavoriteViewHolder {

        final TextView movieTitle;
        final TextView movieRate;
        final TextView movieYear;
        final ImageView moviePoster;
        final ImageButton deleteButton;

        public FavoriteViewHolder(View view) {
            moviePoster = (ImageView) view.findViewById(R.id.movie_favorite_poster);
            movieTitle = (TextView) view.findViewById(R.id.movie_favorite_title);
            movieRate = (TextView) view.findViewById(R.id.movie_favorite_rate);
            movieYear = (TextView) view.findViewById(R.id.movie_favorite_year);
            deleteButton = (ImageButton) view.findViewById(R.id.movie_favorite_delete);

        }
    }

    private void deleteFavoriteFromDatabase(Context context,String movieId){

        final String selection= MovieContract.FavoriteTable.COL_FAVORITE_MOVIE_ID+"=?";

        final String selectionArgs[]=new String[]{movieId};

        int rowCount=context.getContentResolver().delete(MovieContract.FavoriteTable.TABLE_CONTENT_URI,selection,selectionArgs);

        if(rowCount>0){

            Toast.makeText(context, "Movie  Deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
