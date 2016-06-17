package com.hazem.androidnanodegree.thepopularmovie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.objects.Review;

import java.util.List;

/**
 * Created by Mezah on 6/11/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context mContext;
    private List<Review> reviewList;

    private TextView author;
    private TextView content;

    public ReviewAdapter(Context context, int resource, int textViewResourceId, List<Review> reviewList) {
        super(context, resource, textViewResourceId, reviewList);
        mContext=context;
        this.reviewList=reviewList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView= LayoutInflater.from(mContext).inflate(R.layout.review_item,parent,false);
        }

        Review review=reviewList.get(position);

        author= (TextView) convertView.findViewById(R.id.review_author);
        author.setText(review.getAuthor());
        content= (TextView) convertView.findViewById(R.id.review_content);

        content.setText(review.getContent());

        return convertView;
    }
}
