package ru.ryer.droid.PM5;

import android.app.Application;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PmApplication extends Application {

    private final static String TAG = "PmApplication";
    private static final int MAX_GLOBAL_THREADS = Runtime.getRuntime().availableProcessors();

    private static PmApplication singleton; //saved initially in onCreate

    static String postUrl_push = "http://www.ryer.ru/droid/respush.php";
    static String postUrl_pull = "http://www.ryer.ru/droid/respull.php";


    // Database fields


    private ExecutorService oThreadPool;

        //C
        public PmApplication() { //throws SQLException
            super();
            Log.d(TAG,"PmApplication");

            //global thread pool
            oThreadPool = Executors.newFixedThreadPool(MAX_GLOBAL_THREADS);


        }


        @Override
        public void onCreate() {
            super.onCreate();
            singleton = this;       //get the context on the initial create
        }


        public PmApplication getInstance(){
            return singleton;       //dish out the (saved) context as required
        }




        public ExecutorService PmSingletonPool(){
            return oThreadPool;       //return global thread pool
        }




}
