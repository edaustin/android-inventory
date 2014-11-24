package ru.ryer.droid.PM5;

import android.app.Service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PmRESTfulService extends Service {


    //preferences holds whether we have already pushed data
    SharedPreferences sharedPref;
    final private String PREFS = "PM";

    private final static String TAG = "PmRESTfulService";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY; //TODO tbd
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getContentResolver().unregisterContentObserver(oObserver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyLocalBinder();
    }


    public class MyLocalBinder extends Binder{

        PmRESTfulService getMyServices(){

            return PmRESTfulService.this;
        }

    }

    public void registerObserver(){

        //not used Note: If you use Loaders you do not need to listen to changes yourself.
        //In this case Android registers a ContentObserver and triggers your LoaderCallbacks onLoadFinished() method for any changes.


        Runnable rohRunnable = new Runnable() {
            @Override
            public void run() {

                Looper.prepare();
                Handler roh = new Handler(); //for this threads looper


                //observer for the CR/CP URI
                getContentResolver().
                        registerContentObserver(
                                PmContract.CONTENT_URI,
                                true,
                                new ContentObserver(roh) {
                                    @Override
                                    public boolean deliverSelfNotifications() {
                                        return super.deliverSelfNotifications();
                                    }

                                    @Override
                                    public void onChange(boolean selfChange) {
                                        Log.d(TAG,"onChange(bool)");
                                        super.onChange(selfChange);
                                    }

                                    @Override
                                    public void onChange(boolean selfChange, Uri uri) {
                                        Log.d(TAG,"onChange(bool,Uri)");
                                        super.onChange(selfChange, uri);
                                    }
                                });

            }

        };


        //get application
        PmApplication pms = (PmApplication) getApplicationContext();
        pms.PmSingletonPool().execute(rohRunnable);

    }



    public boolean pullDatabaseFromServerUpdateLv(){

        final int HTTP_OK = 200;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //check.. download only once per hour max!

        //remove old database entries prior to re-population
        getContentResolver().delete(PmContract.CONTENT_URI, null, null);


        //pull device data as json

        Runnable pullRunnable = new Runnable() {
            @Override
            public void run() {
                String mPullUrl = PmApplication.postUrl_pull;

                StringBuilder builder = new StringBuilder();
                HttpClient oHttpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(mPullUrl);


                try {
                    HttpResponse oHttpResponse = oHttpClient.execute(httpGet);
                    int rc = oHttpResponse.getStatusLine().getStatusCode();

                    if (rc == HTTP_OK) {
                        HttpEntity entity = oHttpResponse.getEntity();
                        InputStream content = entity.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }

                        //TODO first build don't block retrievals
                        //sharedPref.edit().putBoolean("blocked", true).commit();


                    } else {
                        Log.e(TAG, "Down Response failure " + rc);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Log.d(TAG, "down=" + builder.toString());


                // Parse String to JSON object
                JSONArray jarray=null;
                try { jarray = new JSONArray( builder.toString()); }
                catch (JSONException e) { Log.e("JSON Parser", "Error parsing data " + e.toString()); }

                ContentResolver resolver = getContentResolver();
                ContentValues values = new ContentValues();


                //Log.d(TAG,"Inserting from Server.");
                for (int deviceNo = 0; deviceNo < jarray.length(); deviceNo++)
                    { try
                        {
                            JSONObject c = jarray.getJSONObject(deviceNo);
                            String manuf = c.getString("MANUFACTURER"); //Log.d(TAG,"manuf="+manuf);
                            String model = c.getString("MODEL"); //Log.d(TAG,"model="+model);
                            String hw = c.getString("HARDWARE"); //Log.d(TAG,"hardware="+hw);
                            //String carrier = c.getString("CARRIER"); Log.d(TAG,"carrier="+carrier);
                            String radiov = c.getString("Y"); //Log.d(TAG,"Y="+radiov);
                            String count = c.getString("count"); //Log.d(TAG,"X="+count);

                            //populate ListView via CP insert and observer update
                            values.clear();
                            //values.put(PmSQLiteHelper.COL_ID_0,manuf); //_id
                            values.put(PmSQLiteHelper.COL_ID_1,manuf+" "+model); //c1
                            values.put(PmSQLiteHelper.COL_ID_2,hw);    //c2
                            values.put(PmSQLiteHelper.COL_ID_3,radiov);    //c3
                            values.put(PmSQLiteHelper.COL_ID_4,count);    //c4

                            //Uri uri = ContentUris.withAppendedId(PmContract.CONTENT_URI, id);
                            Uri uri = PmContract.CONTENT_URI;
                            resolver.insert(uri, values);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                //all inserted

                //on insert get the initial database refreshed via a loader callback from the cp query
                getContentResolver().query(PmContract.CONTENT_URI, PmContract.PROJECTION_ALL, null, null, PmContract.SORT_ORDER_DEFAULT);


            }}; //run


        //get application
        PmApplication pms = (PmApplication) getApplicationContext();
        pms.PmSingletonPool().execute(pullRunnable);

        return true; }




    public boolean pushHardwareConfiguration(){

        final int HTTP_OK = 200;

        sharedPref = getSharedPreferences(PREFS, MODE_PRIVATE);


        Runnable pushRunnable = new Runnable() {
            @Override
            public void run() {


                //get device characteristics
                //Log.i("Manufacturer:", Build.MANUFACTURER);
                //Log.i("Board: ", Build.BOARD);
                //Log.i("Device: ", Build.MODEL);
                //Log.i("Hardware: ", Build.HARDWARE);



                // Acquire a reference to the system Location Manager and pull cached only
                //final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //final Location lastKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                //Log.d(TAG,"last="+lastKnown);

                TelephonyManager tmanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                final String carrierName = tmanager.getNetworkOperatorName();
                final String nciso = tmanager.getNetworkCountryIso();
                final String son = tmanager.getSimOperatorName();

                String brv="-";
                if ( android.os.Build.VERSION.SDK_INT >= 14 ) brv = Build.getRadioVersion();


                //unique token
                final String ANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                //build json, not reused
                JSONObject hDescripter = new JSONObject();
                try {
                    hDescripter.put("u", ANDROID_ID);
                    hDescripter.put("m", Build.MANUFACTURER);
                    hDescripter.put("b", Build.BOARD);
                    hDescripter.put("d", Build.MODEL);
                    hDescripter.put("h", Build.HARDWARE);
                    hDescripter.put("c", carrierName);
                    hDescripter.put("x", nciso);
                    hDescripter.put("y", brv);
                    hDescripter.put("z", "-");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println( hDescripter);

                //push device as json
                String mPostUrl = PmApplication.postUrl_push;

                DefaultHttpClient oHttpClient = new DefaultHttpClient();
                HttpPost oPostMethod= new HttpPost(mPostUrl);

                //get db
                try{
                oPostMethod.setEntity(new ByteArrayEntity(hDescripter.toString().getBytes("UTF8")));}
                catch (Exception e) {Log.e(TAG,"ByteArrayEntity data exception");}



                oPostMethod.setHeader("Content-Type", "application/json");

                        //execute online
                        try {
                            Object oResponse = oHttpClient.execute(oPostMethod, new ResponseHandler() { //place the handler as an inner class arg
                                @Override
                                public Object handleResponse(HttpResponse oHttpResponse) throws ClientProtocolException {
                                    int rc = oHttpResponse.getStatusLine().getStatusCode();
                                    //Log.d(TAG,"http response: "+rc);

                                    if (rc==HTTP_OK) { //success

                                        //post the runnable to the ui looper using a handler
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(), "Sent your Hardware Details!", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                        //single use only flag
                                        //set preference if 200 if sent else implicit mark for resend on next run
                                        sharedPref.edit().putBoolean("pushed", true).commit();


                                    }//200
                                    else
                                        Log.e(TAG,"Up Response failure "+rc);
                                    return oHttpResponse;
                                }
                            });




                        } catch (Exception e) {
                            Log.e(TAG,"http exception "+e);}


            }

        }; //runnable

        //get application
        ((PmApplication) getApplicationContext()).PmSingletonPool().execute(pushRunnable);

        return true;


    }

}
