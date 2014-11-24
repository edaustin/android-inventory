package ru.ryer.droid.PM5;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


public class PmContentProvider extends ContentProvider {

    //provides abstract data interface

    private PmSQLiteHelper dbHelper;
    private SQLiteDatabase oDbr;
    private SQLiteDatabase oDbw;

    private final static String TAG = "PmContentProvider";

            @Override
            public boolean onCreate() {

                dbHelper = new PmSQLiteHelper(getContext());
                oDbr = dbHelper.getReadableDatabase();
                oDbw = dbHelper.getWritableDatabase();

                return false;
            }

            @Override
            public Cursor query(Uri uri, String[] projection,
                                String selection, String[] selectionArgs,
                                String sort)
            {
                //Log.d(TAG, "onQuery");
                //Log.d(TAG, "Content Uri="+uri);

                //Uri matcher here - at the moment we don't selectively pull so Uri matcher not useful

                //PmApplication mApplication = (PmApplication) getApplicationContext();
                //SQLiteDatabase db = mApplication.getDB();



                if (oDbr==null)
                        Log.e(TAG,"can't get db handle!");

                //A convenience class to help build the query
                //SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

                // Query the underlying database
                Cursor c = oDbr.query(  PmSQLiteHelper.TABLE_ID,
                                        projection,
                                        selection,
                                        selectionArgs,
                                        null,
                                        null,
                                        sort);

                // Notify the context's ContentResolver if the cursor result set changes
                //Log.d(TAG,"setNotificationUri");
                c.setNotificationUri(getContext().getContentResolver(), uri);


                return c;
            }

            @Override
            public String getType(Uri uri) {
                return null;
            }

            @Override
            public Uri insert(Uri uri, ContentValues contentValues) {


                oDbw.insert(PmSQLiteHelper.TABLE_ID, null, contentValues);

                return null;
            }

            @Override
            public int delete(Uri uri, String s, String[] strings) {


                if (s==null)
                    //Log.d(TAG,"Table delete kludge");
                    oDbw.delete(PmSQLiteHelper.TABLE_ID, null, null);

                return 0;
            }



            @Override
            public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

                return 0;
            }
}
