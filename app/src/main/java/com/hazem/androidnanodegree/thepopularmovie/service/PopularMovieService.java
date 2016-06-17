package com.hazem.androidnanodegree.thepopularmovie.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hazem.androidnanodegree.thepopularmovie.BuildConfig;
import com.hazem.androidnanodegree.thepopularmovie.fragments.MainFragment;
import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;

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

import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_AVERAGE_VOTE;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_BACKDROP_DIR;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_BACKDROP_NAME;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_MOVIE_CATEGORY;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_MOVIE_ID;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_OVERVIEW;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_POSTER_DIR;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_POSTER_NAME;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_RELEASE_DATE;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.COL_TITLE;
import static com.hazem.androidnanodegree.thepopularmovie.data.MovieContract.MovieTable.TABLE_CONTENT_URI;

/**
 * Created by Mezah on 6/14/2016.
 */
public class PopularMovieService extends IntentService {

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String TAG = PopularMovieService.class.getSimpleName();

    public PopularMovieService() {
        super("Movie Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String requiredOrder = intent.getStringExtra(MainFragment.MOVIE_ORDER);

        // creating connection elements
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJSON = null;

        // url used to connect to service
        Uri builder = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(requiredOrder)
                .appendQueryParameter("api_key", BuildConfig.MOVIE_DATABASE_API)
                .build();

        Log.d(TAG, "The complete URL is: " + builder.toString());

        try {

            URL url = new URL(builder.toString());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            InputStreamReader streamReader = new InputStreamReader(inputStream);


            if (inputStream == null) {

                return;

            }

            // reading incoming data
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(streamReader);

            String line;

            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");

            }

            if (buffer.length() == 0) {

                return;

            }

            responseJSON = buffer.toString();

            Log.d(TAG, "JSON Response is: " + responseJSON);

            saveDataFromServer(responseJSON, requiredOrder);

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

        return;
    }

    /**
     * Take the required information from the JSON Object and save it in the database
     *
     * @param json     The JSON Object coming from the web service
     * @param category The order of the movies.
     * @throws JSONException
     */
    private void saveDataFromServer(String json, String category) throws JSONException {

        // elements in the JSON Object
        final String ARRAY_NAME = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_VOTE = "vote_average";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_BACK_DROP = "backdrop_path";

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);

        int arrayLength = jsonArray.length();
        ArrayList<ContentValues> info = new ArrayList<>();

        // reading information from movie array
        for (int i = 0; i < arrayLength; i++) {

            JSONObject mJson = jsonArray.getJSONObject(i);
            // movie id
            String id = mJson.getString(MOVIE_ID).trim();
            // poster name without extension
            String posterName = mJson.getString(MOVIE_POSTER).split("/")[1].split("\\.", -1)[0].trim();  // sM33SANp9z6rXW8Itn7NnG1GOEs;
            // backdrop name without extension
            String backdropName = mJson.getString(MOVIE_BACK_DROP).split("/")[1].split("\\.", -1)[0].trim();  // sM33SANp9z6rXW8Itn7NnG1GOEs;
            // movie title
            String title = mJson.getString(MOVIE_TITLE).trim();
            // average vote
            String rate = mJson.getString(MOVIE_VOTE).trim();
            double vote = Double.valueOf(rate);
            // plot
            String plot = mJson.getString(MOVIE_OVERVIEW).trim();
            // date yyyy-mm-dd
            String date = mJson.getString(MOVIE_RELEASE_DATE).trim();

            String year = date.split("-")[0];
            // create a movie object from th information and add to the list
            PopMovie movie = new PopMovie(id, posterName, backdropName, title, vote, year, plot);

            // save Movies to database
            ContentValues moviesValues = new ContentValues();
            moviesValues.put(COL_MOVIE_ID, movie.getId());
            moviesValues.put(COL_TITLE, movie.getTitle());
            moviesValues.put(COL_OVERVIEW, movie.getPlot());
            moviesValues.put(COL_AVERAGE_VOTE, movie.getRate());
            moviesValues.put(COL_RELEASE_DATE, movie.getYear());
            moviesValues.put(COL_POSTER_NAME, movie.getPosterName());
            moviesValues.put(COL_POSTER_DIR, movie.getPosterUrl());
            moviesValues.put(COL_BACKDROP_NAME, movie.getBackdropName());
            moviesValues.put(COL_BACKDROP_DIR, movie.getBackdropUrl());
            moviesValues.put(COL_MOVIE_CATEGORY, category);

            info.add(moviesValues);

        }

        // insert all values into database
        if (info.size() > 0) {
            ContentValues[] values = new ContentValues[info.size()];
            info.toArray(values);
            getApplicationContext().getContentResolver().bulkInsert(TABLE_CONTENT_URI, values);
        }

    }

}



