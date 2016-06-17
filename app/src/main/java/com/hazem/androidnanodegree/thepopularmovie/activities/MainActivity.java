package com.hazem.androidnanodegree.thepopularmovie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.fragments.DetailFragment;
import com.hazem.androidnanodegree.thepopularmovie.fragments.MainFragment;
import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;
import com.roughike.bottombar.BottomBar;

/**
 * Created by Mezah on 5/7/2016.
 */
public class MainActivity extends AppCompatActivity implements MainFragment.ClickableCallback {

    private static final String POPULAR_MOVIE = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String MAIN_FRAGMENT_TAG="MAIN_TAG";
    private static final String DETAIL_FRAGMENT_TAG="DETAIL_TAG";
    public static final String MOVIE_DETAIL="MOVIE";
    private boolean mTwoPane;
    private Toolbar appToolbar;
    private BottomBar mBottomBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail_flow);

        setToolbar();

        if(findViewById(R.id.movie_details)!=null){

            mTwoPane=true;

            addFragment(new DetailFragment(),R.id.movie_details,DETAIL_FRAGMENT_TAG);

        }else{
            mTwoPane=false;
        }

        addFragment(new MainFragment(),R.id.main_container,MAIN_FRAGMENT_TAG);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.popular:
                getMovies(POPULAR_MOVIE);
                return true;

            case R.id.rated:
                getMovies(TOP_RATED);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMovieSelect(Uri movie) {
        if(mTwoPane){
            // the tablet view is used
            Bundle bundle =new Bundle();
            bundle.putParcelable(MOVIE_DETAIL,movie);
            // add the bundle to the fragment
            DetailFragment detailFragment=DetailFragment.getFragmentInstance(bundle);
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.movie_details,detailFragment,DETAIL_FRAGMENT_TAG)
                    .commit();

        }
        else{
            // the phone is used so use normal intent
            Intent intent=new Intent(MainActivity.this,DetailActivity.class);
            intent.putExtra(MOVIE_DETAIL,movie);
            startActivity(intent);
        }

    }




    private void addFragment(Fragment fragment, int containerID,String fragmentTag){

        // inform main fragment bout the layout
        if(fragmentTag.equals(MAIN_FRAGMENT_TAG)){

            ((MainFragment)fragment).setTwoPaneLayout(mTwoPane);
        }
        // add the fragment
        getSupportFragmentManager()
                .beginTransaction().
                replace(containerID,fragment,fragmentTag)
                .commit();
    }

    private void setToolbar(){
        appToolbar= (Toolbar) findViewById(R.id.main_app_toolbar);
        setSupportActionBar(appToolbar);
        appToolbar.setTitle(getString(R.string.app_name));

    }

    private void getMovies(String order) {
        // get the movie from the movie database
        // get instance of main fragment
        MainFragment mainFragment= (MainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
        // update the list
        mainFragment.updateList(order);
    }



}
