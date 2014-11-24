package ru.ryer.droid.PM5;


import android.content.ContentResolver;
import android.net.Uri;

//final as only holds contract URI constants for the (Content) Resolver/Provider
public final class PmContract {

    private final static String TAG = "PmContracts";

    /**
     Thus the official Android documentation recommends to create a contract class.
     This class defines all publicly available elements, like the authority, the content URIs of your tables, the columns, the content types and also any intents your app offers in addition to your provider.
     */

        /**
         * The authority of the provider.
         */
        public static final String AUTHORITY =  "ru.ryer.droid.PM5.PmCp";


    /**
         * The content URI for the top-level
         * authority. (conforms to RFC 2396.)
    */

    //Typically you should create content URIs from the authority by appending paths that point to individual tables.
    final static String PATH = PmSQLiteHelper.TABLE_ID;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH); //references a table


    /**
     * The mime type of a directory of items.
     */
    public static final String CONTENT_TYPE =ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ru.ryer."+ PmSQLiteHelper.TABLE_ID;
    /**
     * The mime type of a single item.
     */
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ru.ryer."+ PmSQLiteHelper.TABLE_ID;
    /**
     * A projection of all columns
     * in the items table.
     */
    public static final String[] PROJECTION_ALL =  {PmSQLiteHelper.COL_ID_0, PmSQLiteHelper.COL_ID_1, PmSQLiteHelper.COL_ID_2, PmSQLiteHelper.COL_ID_3, PmSQLiteHelper.COL_ID_4};
    /**
     * The default sort order for
     * queries containing NAME fields.
     */
    public static final String SORT_ORDER_DEFAULT = PmSQLiteHelper.COL_ID_0 + " ASC";
}


