package com.bentals.class3;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by ilandbt on 12/11/2015.
 */
public class SimpleLocationActivity extends AppCompatActivity implements LocationListener {

    private final String TAG = getClass().getSimpleName();

    //location
    private LocationManager locationManager;
    private final long SECOND = 1000;
    private final long MIN_DISTANCE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //init location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //check GPS availability
        boolean isGPSAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSAvailable) {

            //get GPS updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SECOND*10, MIN_DISTANCE, this);

        }
    }

    //returns location
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, location.toString());
    }

    //provider status changed
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    //provider enabled by user
    @Override
    public void onProviderEnabled(String provider) {

    }

    //provider disabled by user
    @Override
    public void onProviderDisabled(String provider) {

    }
}
