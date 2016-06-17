package com.hazem.androidnanodegree.thepopularmovie.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.adapters.TrailersAdapter;
import com.hazem.androidnanodegree.thepopularmovie.objects.Trailers;
import com.hazem.androidnanodegree.thepopularmovie.webservice.TrailerFetcher;

import java.util.ArrayList;

/**
 * Created by Mezah on 6/12/2016.
 */
public class TrailerFragment extends Fragment {


    private ListView mTrailerList;
    private TrailersAdapter mTrailerAdapter;
    private String movieId;
    private ImageButton backButton;

    public static TrailerFragment getInstance(Bundle bundle){

        TrailerFragment fragment=new TrailerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieId=getArguments().getString(DetailFragment.MOVIE_ID);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.trailer_fragment_layout,container,false);

        backButton= (ImageButton) view.findViewById(R.id.trailer_return_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to parent previous fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mTrailerList= (ListView) view.findViewById(R.id.trailers_list);

        mTrailerAdapter=new TrailersAdapter(getContext(),
                R.layout.trailer_item,
                0,
                new ArrayList<Trailers>());

        mTrailerList.setAdapter(mTrailerAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        new TrailerFetcher(mTrailerAdapter).execute(movieId);
    }
}
