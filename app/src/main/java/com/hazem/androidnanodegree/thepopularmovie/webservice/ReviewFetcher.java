package com.hazem.androidnanodegree.thepopularmovie.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.hazem.androidnanodegree.thepopularmovie.BuildConfig;
import com.hazem.androidnanodegree.thepopularmovie.adapters.ReviewAdapter;
import com.hazem.androidnanodegree.thepopularmovie.objects.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mezah on 6/11/2016.
 */
public class ReviewFetcher extends AsyncTask<String,Void,ArrayList<Review>> {

    //    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String TAG = ReviewFetcher.class.getSimpleName();
    private static final String REVIEWS="reviews";

    private ReviewAdapter adapter;


    public ReviewFetcher(ReviewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {

        String movieId = params[0];

        // creating connection elements
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJSON = null;
        ArrayList<Review> result = new ArrayList<>();

        // url used to connect to service
        Uri builder = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath(REVIEWS)
                .appendQueryParameter("api_key", BuildConfig.MOVIE_DATABASE_API)
                .build();

        Log.d(TAG, "Review URL is: " + builder.toString());

        try {

            URL url = new URL(builder.toString());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            InputStreamReader streamReader = new InputStreamReader(inputStream);


            if (inputStream == null) {

                return null;

            }

            // reading incoming data
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(streamReader);

            String line;

            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");

            }

            if (buffer.length() == 0) {

                return null;

            }

            responseJSON = buffer.toString();

            Log.d(TAG, "JSON Response is: " + responseJSON);

            result = parseJson(responseJSON);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {

            adapter.clear();
            adapter.addAll(reviews);
            adapter.notifyDataSetChanged();
//
    }

    private ArrayList<Review> parseJson(String json) throws JSONException {

        // elements in the JSON
        final String ARRAY_NAME = "results";
        final String REVIEW_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_NUMBERS="total_results";

        JSONObject jsonObject = new JSONObject(json);
        String nubmerOfReviews=jsonObject.getString(REVIEW_NUMBERS);

        JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);


        int arrayLength = jsonArray.length();
        ArrayList<Review> info = new ArrayList<>();

        // if there no reviews
        if(nubmerOfReviews.equals("0")) {
            Review review=new Review("","","There are No Reviews");
            info.add(review);
        }
        // reading information from movie array
        for (int i = 0; i < arrayLength; i++) {

            JSONObject mJson = jsonArray.getJSONObject(i);
            // review id
            String id = mJson.getString(REVIEW_ID).trim();

            String author = mJson.getString(REVIEW_AUTHOR).trim();

            String content = mJson.getString(REVIEW_CONTENT);
            Log.d("Review",author+"\n"+content);
            Review review = new Review(id, author, content);

            info.add(review);

        }

        return info;
    }
}
