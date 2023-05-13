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
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.unipi.msc.dangerousroads.Databae.UserLocation;
import com.unipi.msc.dangerousroads.Databae.UserLocationService;
import com.unipi.msc.dangerousroads.Model.ItemViewModel;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final int REQ_LOCATION_CODE = 123;
    UserLocationService userLocationService;
    LocationManager locationManager;
    Button buttonMeasure;
    ImageButton imageButtonRefresh;
    UserLocation userLocation;
    private ItemViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonMeasure = findViewById(R.id.buttonMeasure);
        imageButtonRefresh = findViewById(R.id.buttonRefresh);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userLocationService = new UserLocationService(this);
        buttonMeasure.setOnClickListener(this::measuring);
        imageButtonRefresh.setOnClickListener(this::refresh);
        userLocation = null;
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
    }

    private void refresh(View view) {
        viewModel.setRefresh();
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
        viewModel.setStatusLocation(false);
    }

    private void startMeasuring() {
        viewModel.setStatusLocation(true);
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
                        .dateFrom(userLocation.getDateTo())
                        .speedTo(location.getSpeed())
                        .locationTo(new LatLng(location.getLatitude(), location.getLongitude()))
                        .dateTo(new Date().getTime())
                        .build();
            }
            viewModel.selectLocation(userLocation);
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