package com.unipi.msc.dangerousroads.Interfaces;

import com.unipi.msc.dangerousroads.Databae.UserLocation;

import java.util.List;

public interface IUserLocations {
    void returnedLocations(List<UserLocation> userLocationList);
}
