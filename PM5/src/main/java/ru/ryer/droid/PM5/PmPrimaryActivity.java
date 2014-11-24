package ru.ryer.droid.PM5;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;



public class PmPrimaryActivity extends Activity implements LoaderManager.LoaderCallbacks {

    private final static String TAG = "PmPrimaryActivity";

    // This is the Adapter being used to display the list's data
    //SimpleCursorAdapter mAdapter;
    PmCustomCursorAdapter mAdapter;

    //preferences holds whether we have already pushed data
    SharedPreferences sharedPref;
    final private String PREFS = "PM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        assert (1 == 1) : Log.d(TAG, "assert");

        // For the cursor adapter, specify which columns go into which views
        //String[] fromColumns = {PmSQLiteHelper.COL_ID_1, PmSQLiteHelper.COL_ID_2};
        //int[] toViews = {android.R.id.text1, android.R.id.text2}; // The TextView in simple_list_item_2


        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        Cursor NULL_CURSOR = null;

        /*
        mAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                NULL_CURSOR,    //null as loader handles it with mAdapter.swapCursor((Cursor)o);
                fromColumns,    //mapped from
                toViews,        //mapped to
                0);
        */

        mAdapter = new PmCustomCursorAdapter(this,NULL_CURSOR,0);


        ListView mLv = (ListView) findViewById(R.id.alistView);

        //bind the adapter to the view
        mLv.setAdapter(mAdapter);


    }


    @Override
    protected void onStart() {
        //Log.d(TAG, "onStart");
        super.onStart();


    }

    @Override
    protected void onResume() {
        //Log.d(TAG, "onResume");
        super.onResume();

        //initialise Loader
        //Prepare the loader. Either re-connect with an existing one, or start a new one.
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    protected void onPause() {
        //Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        //Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Log.d(TAG, "onDestroy");
        super.onDestroy();
    }


    public void refreshButton(View v){
        //Log.d(TAG,"refresh");
        getLoaderManager().restartLoader(0, null, this); //rewrites the lv
    }




        //Loader implementation

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            //Log.d(TAG, "onCreateLoader");
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.

            CursorLoader oC = new CursorLoader(
                    getBaseContext(),
                    PmContract.CONTENT_URI,
                    PmContract.PROJECTION_ALL,
                    null,
                    null,
                    PmContract.SORT_ORDER_DEFAULT);

            if (oC==null) Log.e(TAG,"null CursorLoader!");
            return oC;
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
                //Log.d(TAG, "onLoadFinished swapping cursor");
                mAdapter.swapCursor((Cursor)o);
        }

        @Override
            public void onLoaderReset(Loader loader) {
                //Log.d(TAG, "onLoaderReset");
                // This is called when the last Cursor provided to onLoadFinished()
                // above is about to be closed.  We need to make sure we are no
                // longer using it.
                mAdapter.swapCursor(null);

        }


}
