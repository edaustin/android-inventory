package ru.ryer.droid.PM5;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class PmSplashActivity extends Activity implements ServiceConnection {

    private final static String TAG = "PmSplashActivity";

    private boolean mBound=false;
    private PmRESTfulService oPmRESTfulService;

    //preferences holds whether we have already pushed data
    SharedPreferences sharedPref;
    final private String PREFS = "PM";
    Boolean blocked=false;
    Boolean pushed=false;


    private int SPLASH_DELAY = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        pushed = sharedPref.getBoolean("pushed",false);
        blocked = sharedPref.getBoolean("blocked",false);


        Intent i=new Intent(this,PmRESTfulService.class); //explicit, implicit would simply be in quotes(") without context
        if (bindService(i, this, BIND_AUTO_CREATE))
        { //Log.d(TAG, "Bind Success");
        } else  {
            //Log.e(TAG,"Bind Fail");
            }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PmSplashActivity.this, PmPrimaryActivity.class));
                //close activity
                finish();
            }
        }, SPLASH_DELAY);
    }


    @Override
    protected void onDestroy() {
        //Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mBound) unbindService(this);
    }

    //implements ServiceConnection


    @Override
    public void onServiceConnected(ComponentName cn, IBinder iBinder) {

        //get exposed services via binder object passed
        PmRESTfulService.MyLocalBinder mbo = (PmRESTfulService.MyLocalBinder) iBinder;
        oPmRESTfulService = mbo.getMyServices();

        mBound=true;

        //oPmRESTfulService.registerObserver();

        if (!pushed)
            oPmRESTfulService.pushHardwareConfiguration();
        //else
        //    Log.d(TAG,"not pushing");


        //place this in a refresh button handler
        if (!blocked) oPmRESTfulService.pullDatabaseFromServerUpdateLv();
        //else
        //    Log.d(TAG, "blocking");

        //try {Thread.sleep(3000);} catch (Exception e) {} //wait until the runnables finish and populate before refreshing the lv
        //getLoaderManager().restartLoader(0, null, this);

    }

    @Override
    public void onServiceDisconnected(ComponentName cn) {
        mBound=false;
    }

}