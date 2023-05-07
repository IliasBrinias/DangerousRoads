package com.unipi.msc.dangerousroads.Databae;

import android.content.Context;

import androidx.room.Room;

import com.unipi.msc.dangerousroads.Constants.Constants;
import com.unipi.msc.dangerousroads.Interfaces.IUserLocations;

public class UserLocationService {
    Context c;
    public UserLocationService(Context c) {
        this.c = c;
    }
    public void getLocations(IUserLocations iUserLocations){
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(c, AppDatabase.class, Constants.DB_NAME).build();
            iUserLocations.returnedLocations(db.locationDao().getAll());
        }).start();
    }
    public void addLocation(UserLocation userLocation){
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(c, AppDatabase.class, Constants.DB_NAME).build();
            db.locationDao().insertLocation(userLocation);
        }).start();
    }
}
