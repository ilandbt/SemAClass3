package com.bentals.class3;

import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

public class MainActivityMarshmallow extends AppCompatActivity implements LocationListener{

    //constants for state saving
    public class Constants {
        public static final String LAST_DISTANCE = "lastDistance";

        public static final String LAST_LOCATION_LAT = "lastLocationLat";
        public static final String LAST_LOCATION_LONG = "lastLocationLong";

        public static final String USER_SATE = "userState";
    }

    private final String TAG = getClass().getSimpleName();

    //views
    private TextView messageDisplay;

    //location
    private LocationManager locationManager;
    private PermissionManager permissionManager;
    private final long SECOND = 1000;
    private final long MIN_DISTANCE = 5;
    private LocationListener mLocationListener;
    private Location lastLocation, targetLocation;
    float lastDistance = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init views
        messageDisplay = (TextView) findViewById(R.id.messageDisplay);

        //init location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = this;

        //Cinema City
        targetLocation = new Location("");
        targetLocation.setLatitude(31.782780);
        targetLocation.setLongitude(35.203695);

        //get saved state
        if (savedInstanceState != null) {
            lastDistance = savedInstanceState.getFloat(Constants.LAST_DISTANCE, -1);

            double lat = savedInstanceState.getDouble(Constants.LAST_LOCATION_LAT, -1);
            double lng = savedInstanceState.getDouble(Constants.LAST_LOCATION_LONG, -1);

            if (lat != -1 && lng != -1){
                lastLocation = new Location("");
                lastLocation.setLatitude(lat);
                lastLocation.setLongitude(lng);
            }

            String state = savedInstanceState.getString(Constants.USER_SATE, getResources().getString(R.string.start));
            messageDisplay.setText(state);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check GPS availability
        boolean isGPSAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSAvailable) {


            //get run time permission
            permissionManager = new PermissionManager(this, new PermissionManager.OnPermissionListener() {
                @Override
                public void OnPermissionChanged(boolean permissionGranted) {
                    Log.d(TAG, "permissionGranted: " + permissionGranted);
                    if (permissionGranted) {
                        try{
                            //get updates
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SECOND*10, MIN_DISTANCE, mLocationListener);
                        } catch (SecurityException e) {   }
                    }

                }
            });

        }

    }

    //returns location
    @Override
    public void onLocationChanged(Location location) {
        //got already a location
        float distance = location.distanceTo(targetLocation);
        if (lastLocation != null) {

            //got already a distance
            //at target
            if (distance < 10){
                messageDisplay.setText(getResources().getString(R.string.here));

            } else {
                //hot
                if (distance < lastDistance) {
                    messageDisplay.setText(getResources().getString(R.string.hotter));
                } else {
                    //cold
                    messageDisplay.setText(getResources().getString(R.string.colder));
                }
            }
        }

        lastDistance = distance;
        lastLocation = new Location(location);
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

    @Override
    protected void onStop() {
        super.onStop();

        try{

            locationManager.removeUpdates(this);

        } catch (SecurityException e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        //save state
        if (lastDistance > 0 && lastLocation != null) {
            outState.putFloat(Constants.LAST_DISTANCE, lastDistance);

            outState.putDouble(Constants.LAST_LOCATION_LAT, lastLocation.getLatitude());
            outState.putDouble(Constants.LAST_LOCATION_LONG, lastLocation.getLongitude());
        }

        outState.putString(Constants.USER_SATE, messageDisplay.getText().toString());

    }
}

