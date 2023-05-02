package com.unipi.msc.dangerousroads.Databae;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("Select * FROM Location")
    List<Location> getAll();
    @Insert
    void insertLocation(Location locations);
    @Delete
    void deleteLocation(Location locations);
}
