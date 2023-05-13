package com.unipi.msc.dangerousroads;

import static com.unipi.msc.dangerousroads.Constants.Constants.MAX_DECELERATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unipi.msc.dangerousroads.Databae.UserLocation;
import com.unipi.msc.dangerousroads.Databae.UserLocationService;
import com.unipi.msc.dangerousroads.Model.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private List<Circle> circles = new ArrayList<>();
    private UserLocationService userLocationService;
    private ItemViewModel viewModel;
    private Boolean gps_status = true;
    private Marker userMarker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        userLocationService = new UserLocationService(getContext());
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    private void loadLocations(){userLocationService.getLocations(this::returnedLocations);}
    private void returnedLocations(List<UserLocation> userLocationList){
        userLocationList.stream().filter(this::check_deceleration).forEach(this::show_circle);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng myMarker = new LatLng(37.9838,23.7275);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker,12f));
        mMap = googleMap;
        loadLocations();
        viewModel.getLocationStatus().observe(requireActivity(),status-> {
            gps_status = status;
            if (userMarker!=null){
                userMarker.setVisible(false);
            }
        });
        viewModel.getRefresh().observe(requireActivity(),r -> {
            circles.forEach(Circle::remove);
            circles.clear();
            loadLocations();
        });

        viewModel.getLocation().observe(requireActivity(),userLocation->{
            if (!gps_status) {
                return;
            }
            if (check_deceleration(userLocation)) show_circle(userLocation);
            if (userMarker==null) {
                userMarker = googleMap.addMarker(new MarkerOptions()
                        .position(userLocation.getLocationTo())
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                );
            }else{
                userMarker.setVisible(true);
                userMarker.setPosition(userLocation.getLocationTo());
            }
        });
    }
    private boolean check_deceleration(UserLocation userLocation){
        long timeDiff = (userLocation.getDateTo()/1000) - (userLocation.getDateFrom()/1000);
        float speedDiff = userLocation.getSpeedTo() - userLocation.getSpeedFrom();
        float acc = speedDiff/timeDiff;
        Log.e("acc", String.valueOf(acc));
        return -acc > MAX_DECELERATION;
    }
    private void show_circle(UserLocation userLocation){
        CircleOptions circleOptions = new CircleOptions()
                .center(userLocation.getLocationTo())
                .radius(100)
                .strokeColor(R.color.black)
                .strokeWidth(5)
                .fillColor(0x15ff0000);
        requireActivity().runOnUiThread(() -> circles.add(mMap.addCircle(circleOptions)));
    }

}