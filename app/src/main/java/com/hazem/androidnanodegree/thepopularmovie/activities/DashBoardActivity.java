package com.hazem.androidnanodegree.thepopularmovie.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.fragments.DetailFragment;
import com.hazem.androidnanodegree.thepopularmovie.fragments.FavoriteFragment;
import com.hazem.androidnanodegree.thepopularmovie.fragments.MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mezah on 6/11/2016.
 */
public class DashBoardActivity extends AppCompatActivity implements
        MainFragment.ClickableCallback,
        FavoriteFragment.FavoriteCallback {

    private static final String POPULAR_MOVIE = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String MAIN_FRAGMENT_TAG = "MAIN_TAG";
    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_TAG";
    public static final String MOVIE_DETAIL = "MOVIE";
    private boolean mTwoPane;
    private ViewPager mFragmentPager;
    private Toolbar mMovieToolbar;
    private TabLayout mMovieTabLayout;
    private MoviePagerAdapter mMoviePagerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail_flow);
        // get layout components
        mMovieToolbar = (Toolbar) findViewById(R.id.main_app_toolbar);
        mMovieTabLayout = (TabLayout) findViewById(R.id.movie_tabs_layout);
        mFragmentPager = (ViewPager) findViewById(R.id.fragment_view_pager);

        // setting toolbar
        setSupportActionBar(mMovieToolbar);
        mMovieToolbar.setTitle(getString(R.string.app_name));
        // check if phone or tablet
        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
            // add the detail fragment to the two pane layout
            addFragment(new DetailFragment(), R.id.detail_container, DETAIL_FRAGMENT_TAG);
        } else {

            mTwoPane = false;
        }

        // setting adapter
        mMoviePagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());
        setPager(mFragmentPager, mMoviePagerAdapter);

        mMovieTabLayout.setupWithViewPager(mFragmentPager);

    }

    /**
     * Adding Fragment to the View pager using the FragmentPagerAdapter
     *
     * @param pager   ViewPager that host the fragments
     * @param adapter Adapter used to populate the fragments.
     */
    private void setPager(ViewPager pager, MoviePagerAdapter adapter) {

        // start main fragment with required movie order
        MainFragment popularFragment = MainFragment.getFragmentInstance(POPULAR_MOVIE);
        MainFragment topRatedFragment = MainFragment.getFragmentInstance(TOP_RATED);
        // add fragments to adapter
        adapter.addFragmentToViewPager(popularFragment, "Popular");

        adapter.addFragmentToViewPager(topRatedFragment, "Top");

        adapter.addFragmentToViewPager(new FavoriteFragment(), "Favorite");
        // attach pager with adapter
        pager.setAdapter(adapter);
    }

    /**
     * Add fragment to specific container.
     *
     * @param fragment    The fragment to be added.
     * @param containerID The container used to host the fragment.
     * @param fragmentTag Identify fragment with unique tag.
     */
    private void addFragment(Fragment fragment, int containerID, String fragmentTag) {

        // inform main fragment bout the layout
        if (fragmentTag.equals(MAIN_FRAGMENT_TAG)) {

            ((MainFragment) fragment).setTwoPaneLayout(mTwoPane);
        }
        // add the fragment
        getSupportFragmentManager()
                .beginTransaction().
                replace(containerID, fragment, fragmentTag)
                .commit();
    }

    // listen to update from detail fragment
    @Override
    public void onMovieSelect(Uri movieUri) {

        navigateMovieDetails(mTwoPane, movieUri);

    }


    // listen to update from favorite fragment
    @Override
    public void onFavoriteSelected(Uri movieUri) {
        navigateMovieDetails(mTwoPane, movieUri);

    }


    /**
     * Determine how the movie detail will be transferred based on device type.
     *
     * @param mTwoPane Check the device type
     *                 true: The device smallest width is larger than 600dp.
     *                 false: The device smallest width is less than 600dp.
     * @param movieUri The movie uri in database with information that will be displayed.
     */
    private void navigateMovieDetails(boolean mTwoPane, Uri movieUri) {
        // go to detail view
        if (mTwoPane) {
            // the tablet view is used
            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE_DETAIL, movieUri);
            // add the bundle to the fragment
            DetailFragment detailFragment = DetailFragment.getFragmentInstance(bundle);
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.detail_container, detailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();

        } else {
            // the phone is used so use normal intent
            Intent intent = new Intent(DashBoardActivity.this, DetailActivity.class);
            intent.putExtra(MOVIE_DETAIL, movieUri);
            startActivity(intent);
        }
    }


    public class MoviePagerAdapter extends FragmentPagerAdapter {


        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();


        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);

        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return fragmentTitles.get(position);
        }


        public void addFragmentToViewPager(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitles.add(title);
        }
    }
}
