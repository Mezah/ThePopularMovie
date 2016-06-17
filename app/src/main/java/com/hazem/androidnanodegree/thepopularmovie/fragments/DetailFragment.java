package com.hazem.androidnanodegree.thepopularmovie.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.activities.DashBoardActivity;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.FavoriteTable;
import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;
import com.squareup.picasso.Picasso;

import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.INDEX_MOVIE_AVERAGE_VOTE;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.INDEX_MOVIE_BACKDROP_DIR;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.INDEX_MOVIE_OVERVIEW;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.INDEX_MOVIE_RELEASE_DATE;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.INDEX_MOVIE_TITLE;

/**
 * Show the detail of each movie based on the user choice in the main view
 * Created by Mezah on 5/7/2016.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String MOVIE_ID = "movie_id";
    public static final String isFavorite = "yes";
    private static final int MOVIE_DETAIL_LOADER = 2;
    private PopMovie movi;
    private Uri movieUri;
    private TextView movieTitle;
    private TextView year;
    private ImageView movieBackdrop;
    private TextView rate;
    private TextView plot;
    private ImageButton favoriteButton;
    private Button reviewButton;
    private Button trailerButton;


    // return an instance of a fragment including movie order information
    public static DetailFragment getFragmentInstance(Bundle bundle) {

        DetailFragment detail = new DetailFragment();
        detail.setArguments(bundle);
        return detail;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        // getting the bundle from main activity
        Bundle args = getArguments();

        // fragment is created for the first or after rotation
        if (args != null) {

            view = inflater.inflate(R.layout.fragment_detail, container, false);

            getLayoutComponents(view);

            movieUri = args.getParcelable(DashBoardActivity.MOVIE_DETAIL);

            initListeners();

        } else {
            // for the first time print welcome
            view = inflater.inflate(R.layout.empty_detail_view, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
    }

    private void getLayoutComponents(View view) {

        movieTitle = (TextView) view.findViewById(R.id.movie_detail_name);
        year = (TextView) view.findViewById(R.id.movie_detail_year);
        movieBackdrop = (ImageView) view.findViewById(R.id.movie_backdrop);
        rate = (TextView) view.findViewById(R.id.movie_detail_rate);
        plot = (TextView) view.findViewById(R.id.movie_detail_plot);
        favoriteButton = (ImageButton) view.findViewById(R.id.movie_detail_favorite);
        reviewButton = (Button) view.findViewById(R.id.review_button);
        trailerButton = (Button) view.findViewById(R.id.trailer_button);

    }

    private void setLayoutDetails(Cursor movieCursor) {

        movieTitle.setText(movieCursor.getString(INDEX_MOVIE_TITLE));
        year.setText(movieCursor.getString(INDEX_MOVIE_RELEASE_DATE));
        Picasso.with(getActivity()).load(movieCursor.getString(INDEX_MOVIE_BACKDROP_DIR)).into(movieBackdrop);
        rate.setText(movieCursor.getString(INDEX_MOVIE_AVERAGE_VOTE));
        plot.setText(movieCursor.getString(INDEX_MOVIE_OVERVIEW));
        if (checkMovieIsFavored(movieUri)) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_black_48dp);
        }
    }


    private void initListeners() {

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save this movie in database
                if (checkMovieIsFavored(movieUri)) {
                    // it is not in database
                    ((ImageButton) v).setImageResource(R.drawable.ic_favorite_border_black_48dp);

                    unFavoredMovie(movieUri);


                } else {

                    saveMovieToFavorite(movieUri);


                    ((ImageButton) v).setImageResource(R.drawable.ic_favorite_black_48dp);


                }
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                final String movieId = MovieContract.getIdFromUri(movieUri);
                bundle.putString(MOVIE_ID, movieId);
                ReviewFragment fragment = ReviewFragment.getInstance(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                final String movieId = MovieContract.getIdFromUri(movieUri);
                bundle.putString(MOVIE_ID, movieId);

                TrailerFragment fragment = TrailerFragment.getInstance(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    // saving movie to database
    private void saveMovieToFavorite(Uri movieUri) {

        final String movieId= MovieContract.getIdFromUri(movieUri);

        ContentValues values=new ContentValues();
        values.put(FavoriteTable.COL_FAVORITE_MOVIE_ID,movieId);
        values.put(FavoriteTable.COL_FAVORITE,isFavorite);

        getActivity().getContentResolver().insert(FavoriteTable.TABLE_CONTENT_URI,values);

        Toast.makeText(getActivity(), "Movie "+movieId+" Saved ", Toast.LENGTH_SHORT).show();

    }

    private void unFavoredMovie(Uri movie) {
        // set the movie in favorite table as unfavored

        final String movieId= MovieContract.getIdFromUri(movieUri);

        final String selection=FavoriteTable.COL_FAVORITE_MOVIE_ID+"=?";

        final String[] selectionArgs=new String[] {movieId};

        getActivity().getContentResolver().delete(FavoriteTable.TABLE_CONTENT_URI,selection,selectionArgs);

    }

    private boolean checkMovieIsFavored(Uri movieUri) {
        // check favorites table
        final String movieId = MovieContract.getIdFromUri(movieUri);

        final String selection=FavoriteTable.COL_FAVORITE_MOVIE_ID+"=?";

        final String[] selectionArgs=new String[] {movieId};

        Cursor cursor = getActivity()
                .getContentResolver()
                .query(FavoriteTable.TABLE_CONTENT_URI,
                        FavoriteTable.TABLE_COLUMNS,
                        selection,
                        selectionArgs,
                        null);

        boolean isFound=cursor.moveToFirst();

        if(isFound){
            String isFavored=cursor.getString(FavoriteTable.INDEX_FAVORITE);
            cursor.close();
            return isFavorite.equals(isFavored);
        }
        cursor.close();
        return isFound;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // show bla
        if (movieUri != null) {
            return new CursorLoader(getActivity(),
                    movieUri,
                    MovieContract.MovieTable.TABLE_COLUMNS,
                    null,
                    null,
                    null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor movieCursor) {
        if (movieCursor != null && movieCursor.moveToFirst()) {
            setLayoutDetails(movieCursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
