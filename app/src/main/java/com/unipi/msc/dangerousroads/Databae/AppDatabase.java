package com.unipi.msc.dangerousroads.Databae;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserLocation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserLocationDao locationDao();
}
