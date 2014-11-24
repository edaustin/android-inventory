package ru.ryer.droid.PM5;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class PmSQLiteHelper extends SQLiteOpenHelper {

    private final static String TAG = "PmSQLiteHelper";

    /**The SQLiteOpenHelper class helps you create databases, and the SQLiteDatabase class is the base class for accessing databases.
    //Used to instantiate, create and populate initial db(s)
    //also added non-abstract CRUD methods here that can be mapped to the abstract Content providers routines rather than hard code them in
    */

    public static final String DB_FILE = "local_0.db";
    public static final int DB_VERSION = 1;

    //define db structure
    public static final String TABLE_ID = "table_0";
    public static final String COL_ID_0 = "_id";
    public static final String COL_ID_1 = "c1";
    public static final String COL_ID_2 = "c2";
    public static final String COL_ID_3 = "c3";
    public static final String COL_ID_4 = "c4";

    //db create string
    final String SQL_CREATE_STRING = "CREATE TABLE "+TABLE_ID+"("+COL_ID_0+" integer PRIMARY KEY, "
            +COL_ID_1+" string, "
            +COL_ID_2+" string, "
            +COL_ID_3+" string, "
            +COL_ID_4+" string)";


    //public PmSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    public PmSQLiteHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
        //Log.d(TAG,"PmSQLiteHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create db using the execSQL sledge-hammer method
        db.execSQL(SQL_CREATE_STRING);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    //@Override
    //public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //    super.onDowngrade(db, oldVersion, newVersion);
    //}

    //usually hidden with other helper stuff but we extended so can see...
    @Override
    public SQLiteDatabase getWritableDatabase() {
        //Log.d(TAG, "getWritableDatabase");
        return super.getWritableDatabase();
    }


}
