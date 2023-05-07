package com.unipi.msc.dangerousroads;

import static com.unipi.msc.dangerousroads.Constants.Constants.ΜΑΧ_DECELERATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unipi.msc.dangerousroads.Constants.Constants;
import com.unipi.msc.dangerousroads.Databae.UserLocation;
import com.unipi.msc.dangerousroads.Databae.UserLocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment {
    GoogleMap mMap;
    List<Circle> circles = new ArrayList<>();
    UserLocationService userLocationService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        userLocationService = new UserLocationService(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                LatLng myMarker = new LatLng(37.9838,23.7275);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker,12f));
                mMap = googleMap;
                userLocationService.getLocations(this::returnedLocations);
            });
        }
    }
    private void returnedLocations(List<UserLocation> userLocationList){
        userLocationList.stream().filter(userLocation -> {
//            TODO: recheck the condition and the acceleration
            float acc = (userLocation.getSpeedTo() - userLocation.getSpeedFrom())/(userLocation.getDateTo() - userLocation.getDateFrom());
            return Math.abs(acc) > ΜΑΧ_DECELERATION;
        }).forEach(userLocation -> {
            CircleOptions circleOptions = new CircleOptions()
                    .center(userLocation.getLocationTo())
                    .radius(100)
                    .fillColor(0x30ff0000);
            requireActivity().runOnUiThread(() -> circles.add(mMap.addCircle(circleOptions)));
        });
    }
}