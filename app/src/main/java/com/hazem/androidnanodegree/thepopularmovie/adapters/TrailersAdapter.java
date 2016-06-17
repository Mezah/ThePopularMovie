package com.hazem.androidnanodegree.thepopularmovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hazem.androidnanodegree.thepopularmovie.R;
import com.hazem.androidnanodegree.thepopularmovie.objects.Trailers;

import java.util.List;

/**
 * Created by Mezah on 6/12/2016.
 */
public class TrailersAdapter extends ArrayAdapter<Trailers> {


    private Context mContext;
    private List<Trailers> trailersList;
    private TextView trailerName;
    private TextView trailerSite;
    ImageButton playButton;

    public TrailersAdapter(Context context, int resource, int textViewResourceId, List<Trailers> list) {
        super(context, resource, textViewResourceId, list);

        mContext=context;
        trailersList=list;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Trailers trailer=trailersList.get(position);

        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.trailer_item,parent,false);
        }

        trailerName= (TextView) convertView.findViewById(R.id.trailer_Name);
        trailerName.setText(trailer.getName());

        trailerSite= (TextView) convertView.findViewById(R.id.trailer_site);
        trailerSite.setText(trailer.getSite());

        playButton=(ImageButton)convertView.findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start an intent to any video view
                Intent intent=new Intent(Intent.ACTION_VIEW,trailer.getVideoUrl());
                mContext.startActivity(intent);
            }
        });


        return convertView;

    }
}
