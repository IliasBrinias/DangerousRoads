package com.unipi.msc.dangerousroads.Databae;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserLocationDao {
    @Query("Select * FROM UserLocation")
    List<UserLocation> getAll();
    @Insert
    void insertLocation(UserLocation locations);
    @Delete
    void deleteLocation(UserLocation locations);
}
