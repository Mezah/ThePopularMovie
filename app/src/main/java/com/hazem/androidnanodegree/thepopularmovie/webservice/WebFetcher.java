package com.hazem.androidnanodegree.thepopularmovie.webservice;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.hazem.androidnanodegree.thepopularmovie.BuildConfig;
import com.hazem.androidnanodegree.thepopularmovie.Utility.MovieUtil;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;
import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
 * Created by Mezah on 5/7/2016.
 */
public class WebFetcher extends AsyncTask<String, Void, ArrayList<PopMovie>> {

    //    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String TAG = WebFetcher.class.getSimpleName();

//    private MoviesAdapter adapter;

    private Context mContext;

//    public WebFetcher(MoviesAdapter adapter) {
//        this.adapter = adapter;
//    }

    public WebFetcher(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<PopMovie> doInBackground(String... params) {

        String requiredOrder = params[0];

        // creating connection elements
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJSON = null;
        ArrayList<PopMovie> result = new ArrayList<>();

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

//            result = parseJson(responseJSON);

            result = getDataFromWeb(responseJSON);

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
    protected void onPostExecute(ArrayList<PopMovie> movies) {
        // Picasso must be on main thread

        if (movies != null) {
            for (PopMovie movie : movies) {
                // save movie poster
                Target posterTarget = MovieUtil.picassoImageTarget(mContext, PopMovie.DIRECTORY, movie.getPoster());
                Picasso.with(mContext).load(movie.getPosterUrl()).into(posterTarget);

                // save backdrop
                Target backdropTarget = MovieUtil.picassoImageTarget(mContext, PopMovie.DIRECTORY, movie.getBackdrop());
                Picasso.with(mContext).load(movie.getBackdropUrl()).into(backdropTarget);
            }


        }

    }

    /**
     * Convert the JSON object the is sent from the server to create a movie object
     *
     * @param json JSON object sent from the Server.
     * @return List of all the movie that returned by the server.
     * @throws JSONException
     */
    private ArrayList<PopMovie> parseJson(String json) throws JSONException {

        // elements in the JSON
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
        ArrayList<PopMovie> info = new ArrayList<>();

        // reading information from movie array
        for (int i = 0; i < arrayLength; i++) {

            JSONObject mJson = jsonArray.getJSONObject(i);
            // movie id
            String id = mJson.getString(MOVIE_ID).trim();
            // poster url
            String poster = mJson.getString(MOVIE_POSTER).trim();
            // backdrop url
            String backdrop = mJson.getString(MOVIE_BACK_DROP).trim();
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
            PopMovie movie = new PopMovie(id, poster, backdrop, title, vote, year, plot);

            info.add(movie);

        }

        return info;
    }

    private ArrayList<PopMovie> getDataFromWeb(String json) throws JSONException {

        // elements in the JSON
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
        ArrayList<ContentValues> enteredData = new ArrayList<>();
        ArrayList<PopMovie> movieList = new ArrayList<>();

        // reading information from movie array
        for (int i = 0; i < arrayLength; i++) {

            JSONObject mJson = jsonArray.getJSONObject(i);
            // movie id
            String id = mJson.getString(MOVIE_ID).trim();
            // poster url
            String poster = mJson.getString(MOVIE_POSTER).trim();
            // backdrop url
            String backdrop = mJson.getString(MOVIE_BACK_DROP).trim();
            // movie title
            String title = mJson.getString(MOVIE_TITLE).trim();
            // average vote
            String rate = mJson.getString(MOVIE_VOTE).trim();
            double theRate = Double.valueOf(rate);
            // plot
            String plot = mJson.getString(MOVIE_OVERVIEW).trim();
            // date yyyy-mm-dd
            String date = mJson.getString(MOVIE_RELEASE_DATE).trim();

            String year = date.split("-")[0];

            PopMovie movie = new PopMovie(id, poster, backdrop, title, theRate, year, plot);
            // TODO Add Code to save each picture into memory and save the url in the database

            // poster path
            String posterPath = MovieUtil.loadImageFromDevice(mContext, poster).getPath();

            // backdrop path
            String backDropPath = MovieUtil.loadImageFromDevice(mContext, backdrop).getPath();

            // create content value for each movie to add it to database
            ContentValues contentValue = new ContentValues();
            contentValue.put(MovieContract.MovieTable.COL_MOVIE_ID, id);
            contentValue.put(MovieContract.MovieTable.COL_TITLE, title);
            contentValue.put(MovieContract.MovieTable.COL_AVERAGE_VOTE, rate);
            contentValue.put(MovieContract.MovieTable.COL_OVERVIEW, plot);
            contentValue.put(MovieContract.MovieTable.COL_RELEASE_DATE, year);
            contentValue.put(MovieContract.MovieTable.COL_POSTER_DIR, posterPath);
            contentValue.put(MovieContract.MovieTable.COL_BACKDROP_DIR, backDropPath);

            enteredData.add(contentValue);
            movieList.add(movie);
        }

        // add all the values to the database
        if (enteredData.size() > 0) {
            ContentValues[] arrayValues = new ContentValues[enteredData.size()];
            enteredData.toArray(arrayValues);
            // insert data
            mContext.getContentResolver().bulkInsert(MovieContract.MovieTable.TABLE_CONTENT_URI, arrayValues);
        }

        return movieList;
    }


}
