package com.hazem.androidnanodegree.thepopularmovie.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.AdapterView;
import android.widget.GridView;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.adapters.MovieCursorAdapter;
import com.hazem.androidnanodegree.thepopularmovie.adapters.MoviesAdapter;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;
import com.hazem.androidnanodegree.thepopularmovie.service.PopularMovieService;

/**
 * Created by Mezah on 5/7/2016.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String MOVIE_ORDER = "order";
    private static final int MOVIE_LOADER_ID=1;

    private ClickableCallback mClickCallback;
    private String movieOrder;
    private GridView mMovieGrid;
    private MovieCursorAdapter movieCursor;
    private boolean mTwoPane;

    public static MainFragment getFragmentInstance(String movieOrder) {

        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_ORDER, movieOrder);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // get information from activity;
        mClickCallback = (ClickableCallback) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle args = getArguments();
        movieOrder=args.getString(MOVIE_ORDER);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // check for device orientation
        int orientation = getResources().getConfiguration().orientation;

        mMovieGrid = (GridView) view.findViewById(R.id.movie_grid);

        setGridView(mTwoPane, orientation);

        movieCursor=new MovieCursorAdapter(getContext(),null,0);

        // setting cursor adapter
        mMovieGrid.setAdapter(movieCursor);

        mMovieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get the movie based on user click to send its information with the intent
                // to display it in the detail activity
                Cursor movieCursor = (Cursor) parent.getAdapter().getItem(position);

                Uri movieUri= MovieContract.MovieTable.buildMovieWithId(movieCursor.getString(MovieContract.MovieTable.INDEX_MOVIE_ID));

                mClickCallback.onMovieSelect(movieUri);



            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);

    }

    @Override
    public void onStart() {
        super.onStart();

        updateList(movieOrder);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mClickCallback = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri tableUri= MovieContract.MovieTable.TABLE_CONTENT_URI;

        final String selection= MovieContract.MovieTable.COL_MOVIE_CATEGORY+"=?";

        final String[] selectionArgs=new String[]{movieOrder};

        return new CursorLoader(getActivity(),
                tableUri,
                MovieContract.MovieTable.TABLE_COLUMNS,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        movieCursor.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieCursor.swapCursor(null);
    }


    // this interface is implemented in any activity required to communicate with this fragment
    public interface ClickableCallback {
        void onMovieSelect(Uri movieUri);
    }


    /**
     * Update the order of the movie displayed in the grid view
     *
     * @param order The order of the movies based on menu selection in main activity that
     *              host the fragment
     */
    public void updateList(String order) {

        // launch the service
        Intent serviceIntent=new Intent(getActivity(),PopularMovieService.class);
        serviceIntent.putExtra(MainFragment.MOVIE_ORDER,order);
        getActivity().startService(serviceIntent);

    }

    /**
     * Set the device type.
     *
     * @param twoPaneMode this boolean value determine the device type
     *                    true: the layout is two pane layout (Maser-detail layout)
     *                    false: It is a phone layout
     */
    public void setTwoPaneLayout(boolean twoPaneMode) {
        mTwoPane = twoPaneMode;
    }


    /**
     * Set GridView parameter based on orientation and device type
     *
     * @param isTwoPane   Check device type.
     * @param orientation Check device orientation.
     */
    private void setGridView(boolean isTwoPane, int orientation) {

        final int portrait = Configuration.ORIENTATION_PORTRAIT;
        final int landscape = Configuration.ORIENTATION_LANDSCAPE;
        int verticalSpacing = (int) getResources().getDimension(R.dimen.grid_view_vertical_spacing);
        // set parameters for each orientation
        if (orientation == portrait) {
            mMovieGrid.setVerticalSpacing(verticalSpacing);
            mMovieGrid.setNumColumns(GridView.AUTO_FIT);
        }

        if (isTwoPane || orientation == landscape) {
            mMovieGrid.setColumnWidth(100);
            mMovieGrid.setNumColumns(3);
        }

    }

}
