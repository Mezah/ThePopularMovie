package com.hazem.androidnanodegree.thepopularmovie;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.hazem.androidnanodegree.thepopularmovie.data.MovieContentProvider;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieContract;
import com.hazem.androidnanodegree.thepopularmovie.data.MovieSQLiteHelper;
import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;

import java.util.List;

/**
 * Created by Mezah on 5/31/2016.
 */
public class MovieProviderTest extends AndroidTestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // delete data after evert test
        deleteEntries();
    }

    /**
     * Test the registration of the content provider
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieContentProvider.class.getName());

        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);

            Log.d("AUTOHRITY", providerInfo.authority);
            Log.d("AUTOHRITY",MovieContract.CONTENT_AUTHORITY);

        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testProviderGetType(){

        // get the type of the  content

        // content://com.hazem.androidnanodegree.thepopularmovie/movies
        String actual_type_base=mContext.getContentResolver().getType(MovieContract.MovieTable.TABLE_CONTENT_URI);

        // vnd.android.cursor.dir/com.hazem.androidnanodegree.thepopularmovie/movies
        String expected_type_base=MovieContract.MovieTable.TABLE_CONTENT_TYPE;

        assertEquals("Error: Wrong Content type doesn't match",expected_type_base,actual_type_base);

        double rate=12.5;
        String actual_type_item=mContext.getContentResolver().getType(MovieContract.MovieTable.buildMovieUriWithRate(rate));

        String expected_type_item=MovieContract.MovieTable.TABLE_ITEM_CONTENT_TYPE;

        assertEquals("Error: Wrong Content type doesn't match",expected_type_item,actual_type_item);

    }

    public void testProviderQuery(){

        // Table uri to be used
        final Uri tableUri=MovieContract.MovieTable.TABLE_CONTENT_URI;

        // the table name to be used
        String tableName= MovieContract.MovieTable.TABLE_NAME;

        // get instance of database
        final SQLiteDatabase db=new MovieSQLiteHelper(mContext).getWritableDatabase();

        // get a content resolver
        ContentResolver resolver=mContext.getContentResolver();

        // get value to be inserted
        ContentValues values=TestUtility.createDatabaseEntries();

        // insert the values to the database
        long rowId=db.insert(tableName,null,values);

        // check the insertion
        assertTrue("Error: Unable to insert Entries to the database",rowId!=-1);

        // close the database
        db.close();

        /////////////////////////////
        // Query the data

        Cursor cursor=resolver.query(tableUri,
                null, // read all columns
                null,
                null,
                null
                );

        // validate the data from the cursor with the data entered from content values
        TestUtility.cursorValidation("Testing Insertion. Error in data Validation",cursor,values);



    }

    public void testProviderInsertion(){

        // values to be inserted
        ContentValues values=TestUtility.createDatabaseEntries();
        // table uri
        final Uri movieUrl=MovieContract.MovieTable.TABLE_CONTENT_URI;
        // content resolver
        ContentResolver resolver =mContext.getContentResolver();

        // create content observer to monitor the insertion of data
        TestUtility.TestContentObserver observer=TestUtility.getTestContentObserver();
        // register the content observer with the uri via content resolver
        resolver.registerContentObserver(movieUrl,true,observer);
        // ================ data insertion phase ====================================
        // insert data in database
        Uri rowUri=resolver.insert(movieUrl,values);

        // wait for 5 sec for notification
        observer.waitForNotificationOrFail();
        // unregister the observer
        resolver.unregisterContentObserver(observer);

        //======================  Data retrieval and check phase ============================

        // get data from database
        Cursor cursor=resolver.query(movieUrl,null,null,null,null);

        // validate data from cursor with data from contentValues
        TestUtility.cursorValidation("Problem in data validation after insertion",cursor,values);

    }

    public void testUpdating(){
        // create value to be inserted
        ContentValues values= TestUtility.createDatabaseEntries();

        // table uri
        final Uri tableUri= MovieContract.MovieTable.TABLE_CONTENT_URI;

        // get content resolver
        ContentResolver resolver=mContext.getContentResolver();

        // insert data into database
        Uri moveUri=resolver.insert(tableUri,values);

        // create new entry and add it to the database
        ContentValues newValue=new ContentValues();
        newValue.put(MovieContract.MovieTable.COL_MOVIE_ID, "new Id");
        newValue.put(MovieContract.MovieTable.COL_TITLE, "New Title");
        newValue.put(MovieContract.MovieTable.COL_OVERVIEW, "New Plot");
        newValue.put(MovieContract.MovieTable.COL_RELEASE_DATE, "New Year");
        newValue.put(MovieContract.MovieTable.COL_AVERAGE_VOTE, "New Rate");


        // create a cursor that points to the data
        Cursor cursor=resolver.query(tableUri,null,null,null,null);

        // create an observer
        TestUtility.TestContentObserver observer=TestUtility.getTestContentObserver();

        // register the cursor in the observer
        cursor.registerContentObserver(observer);

        // update the data in database with the new entry
        long count=resolver.update(tableUri,newValue,null,null);

        // check the insertion
        assertEquals("",1,count);

        // check the observer
        observer.waitForNotificationOrFail();

        // unregister and close cursor
        cursor.unregisterContentObserver(observer);
        cursor.close();

        // check the data in database

        // add the new value to the contentValues
        values.putAll(newValue);

        // query the data
        Cursor mCursor=resolver.query(tableUri,null,null,null,null);

        // check the data
        TestUtility.cursorValidation("Problem after update data, some entry are missing",mCursor,values);
    }

    public void deleteEntries(){

        ContentResolver resolver=mContext.getContentResolver();

        int id=resolver.delete(MovieContract.MovieTable.TABLE_CONTENT_URI,null,null);

        assertTrue("Error: Unable to delete item",id>0);
    }

    public void testDeleteMethod(){

        // insert data into database
        testProviderInsertion();

        // get contentResolver
        ContentResolver resolver=mContext.getContentResolver();

        // table uri
        final Uri tableUri=MovieContract.MovieTable.TABLE_CONTENT_URI;

        // register with contentObserver
        TestUtility.TestContentObserver observer=TestUtility.getTestContentObserver();
        resolver.registerContentObserver(tableUri,true,observer);


        // delete data from table
        mContext.getContentResolver().delete(tableUri,null,null);

        observer.waitForNotificationOrFail();

       // unregister observer
        resolver.unregisterContentObserver(observer);

    }

    public void testIfUserAddMovieBefore(){

        ContentValues values=TestUtility.createDatabaseEntries();

        List<PopMovie> list=TestUtility.movieList();

        // get contentResolver
        ContentResolver resolver=mContext.getContentResolver();


        // table uri
        final Uri tableUri=MovieContract.MovieTable.TABLE_CONTENT_URI;

        resolver.insert(tableUri,values);

        String[] projection={MovieContract.MovieTable.COL_MOVIE_ID};

        String where=MovieContract.MovieTable.COL_MOVIE_ID+" = ?";

        String[] args={list.get(14).getId()};

        Cursor cursor=resolver.query(tableUri,null,null,null,null);

        Cursor cursor1=resolver.query(tableUri,projection,where,args,null);

        Cursor cursor2=resolver.query(tableUri,projection,(MovieContract.MovieTable.COL_MOVIE_ID+" = ? "),new String[]{"17"},null);

        int index=cursor.getColumnIndex(MovieContract.MovieTable.COL_MOVIE_ID);

        assertTrue("Error: Can't find the column in cursor 1",index!=-1);

        boolean isFound=cursor.moveToFirst();
        boolean isFound1=cursor1.moveToFirst();
        boolean isFound2=cursor2.moveToFirst();


        assertTrue("Error: Can't find the item",isFound);
        assertTrue("Error: Can't find the item",isFound1);
        assertTrue("Error: Can't find the item",!isFound2);


        cursor.close();




    }

}
