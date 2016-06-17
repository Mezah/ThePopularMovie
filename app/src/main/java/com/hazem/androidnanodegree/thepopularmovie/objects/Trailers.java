package com.hazem.androidnanodegree.thepopularmovie.objects;

import android.net.Uri;

/**
 * Created by Mezah on 6/12/2016.
 */
public class Trailers {


    private static final String BASE_URL="https://www.youtube.com/watch?";
    private String name;
    private String site;
    private String key;


    public Trailers(String name, String site, String key) {
        this.name = name;
        this.site = site;
        this.key = key;
    }


    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }

    public Uri getVideoUrl(){

        Uri videoUri=Uri.parse(BASE_URL).buildUpon().appendQueryParameter("v",key).build();

        return videoUri;
    }
}
