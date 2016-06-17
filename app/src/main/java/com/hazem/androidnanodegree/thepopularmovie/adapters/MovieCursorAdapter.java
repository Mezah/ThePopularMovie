package com.hazem.androidnanodegree.thepopularmovie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Mezah on 6/14/2016.
 */
public class MovieCursorAdapter extends CursorAdapter {


    private Context mContext;

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // inflate view
        View view= LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);

        MovieViewHolder viewHolder=new MovieViewHolder(view);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        MovieViewHolder viewHolder=(MovieViewHolder)view.getTag();

        String movieTitle=cursor.getString(MovieContract.MovieTable.INDEX_MOVIE_TITLE);

        String moviePosterDir=cursor.getString(MovieContract.MovieTable.INDEX_MOVIE_POSTER_DIR);

        viewHolder.movieTitle.setText(movieTitle);
        Picasso.with(mContext).load(moviePosterDir).into(viewHolder.moviePoster);

    }


    static class MovieViewHolder{

        final ImageView moviePoster;
        final TextView movieTitle;

        public MovieViewHolder(View view) {
            movieTitle = (TextView) view.findViewById(R.id.movie_main_title);
            moviePoster = (ImageView) view.findViewById(R.id.main_movie_poster);
        }
    }
}
