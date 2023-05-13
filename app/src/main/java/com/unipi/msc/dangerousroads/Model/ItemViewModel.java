package com.unipi.msc.dangerousroads.Model;

import android.content.ClipData.Item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unipi.msc.dangerousroads.Databae.UserLocation;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<UserLocation> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refresh = new MutableLiveData<>();
    public void selectLocation(UserLocation location) {
        selectedLocation.setValue(location);
    }
    public LiveData<UserLocation> getLocation() {
        return selectedLocation;
    }
    public void setStatusLocation(Boolean status) {
        locationStatus.setValue(status);
    }
    public LiveData<Boolean> getLocationStatus() {
        return locationStatus;
    }
    public void setRefresh() {
        refresh.setValue(true);
    }
    public LiveData<Boolean> getRefresh() {
        return refresh;
    }

}
