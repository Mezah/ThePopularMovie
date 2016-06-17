package com.hazem.androidnanodegree.thepopularmovie.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.hazem.androidnanodegree.thepopularmovie.BuildConfig;
import com.hazem.androidnanodegree.thepopularmovie.adapters.TrailersAdapter;
import com.hazem.androidnanodegree.thepopularmovie.objects.Trailers;

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
public class TrailerFetcher extends AsyncTask<String,Void,ArrayList<Trailers>> {


    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String TAG = TrailerFetcher.class.getSimpleName();
    private static final String VIDEOS="videos";
    private TrailersAdapter mTrailerAdapter;

    public TrailerFetcher(TrailersAdapter adaoter) {
        mTrailerAdapter=adaoter;
    }

    @Override
    protected ArrayList<Trailers> doInBackground(String... params) {
        String movieId = params[0];

        // creating connection elements
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJSON = null;
        ArrayList<Trailers> result = new ArrayList<>();

        // url used to connect to service
        Uri builder = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath(VIDEOS)
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
    protected void onPostExecute(ArrayList<Trailers> trailers) {

        mTrailerAdapter.clear();
        mTrailerAdapter.addAll(trailers);
        mTrailerAdapter.notifyDataSetChanged();

    }

    private ArrayList<Trailers> parseJson(String json) throws JSONException {

        // elements in the JSON
        final String ARRAY_NAME = "results";
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
        final String TRAILER_SITE = "site";

        JSONObject jsonObject = new JSONObject(json);

        JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);


        int arrayLength = jsonArray.length();
        ArrayList<Trailers> info = new ArrayList<>();

        // reading information from movie array
        for (int i = 0; i < arrayLength; i++) {

            JSONObject mJson = jsonArray.getJSONObject(i);
            // review id
            String key = mJson.getString(TRAILER_KEY).trim();

            String name = mJson.getString(TRAILER_NAME).trim();

            String site = mJson.getString(TRAILER_SITE).trim();

            Trailers trailer = new Trailers(name, site, key);

            info.add(trailer);

        }

        return info;
    }
}
