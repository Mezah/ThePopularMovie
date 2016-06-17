package com.hazem.androidnanodegree.thepopularmovie.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * The adapter that is used to map the movie to the grid view
 * Created by Mezah on 5/7/2016.
 */
public class MoviesAdapter extends ArrayAdapter<PopMovie> {


    private Context context;
    // the list of the movies to be shown
    private List<PopMovie> moviesList;

    public MoviesAdapter(Context context, int resource, int textViewResourceId, List<PopMovie> moviesList) {
        super(context, resource, textViewResourceId, moviesList);

        this.moviesList = moviesList;

        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the movie at certain position in the list
        PopMovie movie = moviesList.get(position);

        if (convertView == null) {
            // inflate the view
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        }

        ImageView text = (ImageView) convertView.findViewById(R.id.main_movie_poster);

        TextView movieName=(TextView)convertView.findViewById(R.id.movie_main_title);
        // populate the movie poster based on the position using Picasso
        Picasso.with(context).load(movie.getPosterUrl()).into(text);
        movieName.setText(movie.getTitle());

        return convertView;

    }


}
