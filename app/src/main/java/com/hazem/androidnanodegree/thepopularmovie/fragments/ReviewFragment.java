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
import com.hazem.androidnanodegree.thepopularmovie.adapters.ReviewAdapter;
import com.hazem.androidnanodegree.thepopularmovie.objects.Review;
import com.hazem.androidnanodegree.thepopularmovie.webservice.ReviewFetcher;

import java.util.ArrayList;

/**
 * Created by Mezah on 6/11/2016.
 */
public class ReviewFragment extends Fragment {


    private ListView reviewList;
    private ReviewAdapter reviewAdapter;
    private String movieId;
    private ImageButton backButton;

    public static ReviewFragment getInstance(Bundle bundle){

        ReviewFragment fragment=new ReviewFragment();
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

        View view=inflater.inflate(R.layout.review_fragment_layout,container,false);

        backButton= (ImageButton) view.findViewById(R.id.review_return_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to parent previous fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        reviewList= (ListView) view.findViewById(R.id.reviews_list);


        reviewAdapter=new ReviewAdapter(getContext(),
                R.layout.review_item,
                0,
                new ArrayList<Review>());

        reviewList.setAdapter(reviewAdapter);


        return view;
    }

    private void getReviews(String id){
        new ReviewFetcher(reviewAdapter).execute(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        getReviews(movieId);
    }
}
