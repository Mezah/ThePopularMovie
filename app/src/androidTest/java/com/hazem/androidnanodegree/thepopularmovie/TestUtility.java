package com.hazem.androidnanodegree.thepopularmovie;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;
import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by Mezah on 6/4/2016.
 */
public class TestUtility extends AndroidTestCase {


    public static ContentValues createDatabaseEntries() {
        ContentValues values = new ContentValues();
        List<PopMovie> list = movieList();

        for (PopMovie movie : list) {
            values.put(MovieContract.MovieTable.COL_MOVIE_ID, movie.getId());
            values.put(MovieContract.MovieTable.COL_TITLE, movie.getTitle());
            values.put(MovieContract.MovieTable.COL_OVERVIEW, movie.getPlot());
            values.put(MovieContract.MovieTable.COL_RELEASE_DATE, movie.getYear());
            values.put(MovieContract.MovieTable.COL_AVERAGE_VOTE, movie.getAverageRate());
        }

        return values;

    }


    public static List<PopMovie> movieList() {

        ArrayList<PopMovie> list = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            String id = Integer.toString(i);
            String posterUrl = "www.movie_poster_" + i + ".jpg";
            String backdropUrl = "www.movie_backdrop_" + i + ".jpg";
            String title = "Movie_" + i;
            double rate = 2.2 * i;
            String year = "movie_" + i + "year";
            String plot = "Movie_" + i + " The Plot";
            PopMovie movie = new PopMovie(id, posterUrl, backdropUrl, title, rate, year, plot);

            list.add(movie);

        }

        return list;
    }


    public static void cursorValidation(String error, Cursor actualDataCursor, ContentValues expectedData) {

        // check that there is data
        assertTrue("The Cursor is empty. " + error, actualDataCursor.moveToFirst());
        // check data
        cursorDataValidation(error, actualDataCursor, expectedData);
        // close cursor
        actualDataCursor.close();
    }

    public static void cursorDataValidation(String error, Cursor actualDataCursor, ContentValues expectedData) {

        // get the data in the content value
        Set<Map.Entry<String, Object>> expectedValueSet = expectedData.valueSet();

        // looping around the data set
        for (Map.Entry<String, Object> entry : expectedValueSet) {
            // col name from the values
            String colName = entry.getKey();
            // get the index from the cursor
            int colIndex = actualDataCursor.getColumnIndex(colName);

            // Check if the column is retrieved from database via cursor
            assertFalse("The Column [ " + colName + " ] is not found " + error, colIndex == -1);

            // get the entered value
            String expectedEntry = entry.getValue().toString();
            // get the entry from cursor
            String actualValue = actualDataCursor.getString(colIndex);

            // compare it with the value from cursor
            assertEquals("The values [ " + expectedEntry + " ] and " + actualValue + " are not the same. ", expectedEntry, actualValue);


        }
    }


    static class TestContentObserver extends ContentObserver {

        final HandlerThread mHT;

        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            // this method will be performed if a change is occurred to the content provider

            // this value will be true only if a change is occurred in the content provider
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.



            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    // this method is running in a different thread for 5 seconds (5000 millisecond)
                    // while testing the mContentChanged boolean if a change occurs before the
                    // five seconds this value will return or the thread will continue to run till
                    // the five seconds are finished the a value false will be returned
                    // an assertion is used in the PollingCheck main class to make sure a warning
                    // will be triggered if the values mContentChecked is not true
                    // if the value is true the method return with no problem
                    return mContentChanged;
                }
            }.run();
            mHT.quit();

        }
    }



    static TestContentObserver getTestContentObserver() {

        return TestContentObserver.getTestContentObserver();

    }


}

// from sunshine
 abstract class PollingCheck {

    private static final long TIME_SLICE = 50;
    private long mTimeout = 3000;

    public PollingCheck() {
    }

    public PollingCheck(long timeout) {
        mTimeout = timeout;
    }

    protected abstract boolean check();

    public void run() {
        if (check()) {
            return;
        }

        long timeout = mTimeout;
        while (timeout > 0) {
            try {
                Thread.sleep(TIME_SLICE);
            } catch (InterruptedException e) {
                Assert.fail("unexpected InterruptedException");
            }

            if (check()) {
                return;
            }

            timeout -= TIME_SLICE;
        }

        Assert.fail("unexpected timeout");
    }

    public static void check(CharSequence message, long timeout, Callable<Boolean> condition)
            throws Exception {
        while (timeout > 0) {
            if (condition.call()) {
                return;
            }

            Thread.sleep(TIME_SLICE);
            timeout -= TIME_SLICE;
        }

        Assert.fail(message.toString());
    }
}
