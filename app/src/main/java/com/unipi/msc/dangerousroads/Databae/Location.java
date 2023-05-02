package com.unipi.msc.dangerousroads.Databae;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo
    private Long date;
    @ColumnInfo
    private double location;
    @ColumnInfo
    private double speed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double location) {
        this.location = location;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Location(Long id, Long date, double location, double speed) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.speed = speed;
    }

    @Ignore
    public Location(Long date, double location, double speed) {
        this.date = date;
        this.location = location;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", date=" + date +
                ", location=" + location +
                ", speed=" + speed +
                '}';
    }
}
