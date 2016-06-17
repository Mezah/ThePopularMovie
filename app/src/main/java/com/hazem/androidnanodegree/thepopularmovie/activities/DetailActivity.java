package com.hazem.androidnanodegree.thepopularmovie.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.fragments.DetailFragment;

/**
 * Created by Mezah on 5/7/2016.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String DETAIL_FRAGMENT_TAG="DETAIL_TAG";
    private Toolbar appToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        // start the detail fragment with movie information
        Bundle args=new Bundle();
        args.putParcelable(DashBoardActivity.MOVIE_DETAIL,getIntent().getParcelableExtra(DashBoardActivity.MOVIE_DETAIL));

        setToolbar();

        DetailFragment fragment=DetailFragment.getFragmentInstance(args);

        if(savedInstanceState==null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_container,fragment,DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }

    // setting the toolbar
    private void setToolbar() {

        appToolbar = (Toolbar)findViewById(R.id.main_app_toolbar);

        setSupportActionBar(appToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
