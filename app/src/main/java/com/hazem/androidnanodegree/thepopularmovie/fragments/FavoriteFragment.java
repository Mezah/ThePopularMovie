package com.hazem.androidnanodegree.thepopularmovie.fragments;

import android.content.Context;
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
import android.widget.ListView;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.adapters.FavoriteCursorAdapter;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;

/**
 * Created by Mezah on 6/10/2016.
 */
public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FAVORITE_LOADER = 3;
    private static final String FAVORITE_ID = "1";

    private ListView favoriteList;
    private FavoriteCursorAdapter mFavoriteCursor;
    private FavoriteCallback mFavCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mFavCallback = (FavoriteCallback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favorite_fragment, container, false);

        favoriteList = (ListView) view.findViewById(R.id.movie_favorite_list);
        // the call back is used to communicate with detail activity
        mFavoriteCursor = new FavoriteCursorAdapter(getActivity(), null, FAVORITE_LOADER, mFavCallback);

        favoriteList.setAdapter(mFavoriteCursor);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // start loader
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // uri to the joined table
        // this uri is not related to any movie id
        Uri joinedTableUri = MovieContract.FavoriteTable.buildFavoriteWithId(FAVORITE_ID);

        return new CursorLoader(getActivity(),
                joinedTableUri,
                MovieContract.FavoriteTable.JOINED_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor favoriteCursor) {
        // attached loader
        mFavoriteCursor.swapCursor(favoriteCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteCursor.swapCursor(null);
    }

    public interface FavoriteCallback {
        void onFavoriteSelected(Uri movieUri);
    }

}
