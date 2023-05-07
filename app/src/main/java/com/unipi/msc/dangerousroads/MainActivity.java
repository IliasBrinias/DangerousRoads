package com.unipi.msc.dangerousroads;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.unipi.msc.dangerousroads.Databae.UserLocation;
import com.unipi.msc.dangerousroads.Databae.UserLocationService;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final int REQ_LOCATION_CODE = 123;
    UserLocationService userLocationService;
    LocationManager locationManager;
    Button buttonMeasure;
    UserLocation userLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonMeasure = findViewById(R.id.buttonMeasure);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userLocationService = new UserLocationService(this);
        buttonMeasure.setOnClickListener(this::measuring);
        userLocation = null;
    }
    private void measuring(View v){
        if (buttonMeasure.getText().equals(getString(R.string.start_measuring))){
            startMeasuring();
        }else {
            stopMeasuring();
        }
    }
    private void stopMeasuring() {
        buttonMeasure.setText(getString(R.string.start_measuring));
        locationManager.removeUpdates(this);
    }

    private void startMeasuring() {
        buttonMeasure.setText(getString(R.string.stop_measuring));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },REQ_LOCATION_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, this);
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
//        TODO: send data to fragment activity for
//         1) current location
//         2) new db data
        if (userLocation == null){
            userLocation = new UserLocation.UserLocationBuilder()
                    .speedFrom(location.getSpeed())
                    .locationFrom(new LatLng(location.getLatitude(),location.getLongitude()))
                    .dateFrom(new Date().getTime())
                    .build();
        }else {
            if (userLocation.getLocationTo() == null) {
                userLocation.setLocationTo(new LatLng(location.getLatitude(), location.getLongitude()));
                userLocation.setDateTo(new Date().getTime());
                userLocation.setSpeedTo(location.getSpeed());
            }else {
                userLocation = new UserLocation.UserLocationBuilder()
                        .speedFrom(userLocation.getSpeedFrom())
                        .locationFrom(userLocation.getLocationTo())
                        .dateFrom(userLocation.getDateFrom())
                        .speedTo(location.getSpeed())
                        .locationTo(new LatLng(location.getLatitude(), location.getLongitude()))
                        .dateTo(new Date().getTime())
                        .build();
            }
            userLocationService.addLocation(userLocation);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }
}