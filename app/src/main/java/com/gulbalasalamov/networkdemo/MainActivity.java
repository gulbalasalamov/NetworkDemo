package com.gulbalasalamov.networkdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static TextView tv_place;
    static TextView tv_temperature;

    private Criteria criteria;
    private MyLocationListener locationListener;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_place = (TextView) findViewById(R.id.place);
        tv_temperature = (TextView) findViewById(R.id.temperature);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        // user defines the criteria

        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);

        // the last known location of this provider
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        locationListener = new MyLocationListener();

        if (location != null) {
            locationListener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 200, 1, locationListener);
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        Weather task = new Weather();
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(lat) +
                "&lon=" + String.valueOf(lon) + "&appid=2c7df8b43d775f828db1e9167a20a5ff");
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            Toast.makeText(MainActivity.this,  ""+location.getLatitude()+", "+location.getLongitude(),
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, provider + "'durumu değişti: "+status +"!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " etkinleştirildi!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " etkin değil!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
