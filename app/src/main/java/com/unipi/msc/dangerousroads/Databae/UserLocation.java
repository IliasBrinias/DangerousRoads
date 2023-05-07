package com.unipi.msc.dangerousroads.Databae;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import kotlin.BuilderInference;

@Entity
public class UserLocation {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long dateFrom;
    private Long dateTo;
    private double locationFromX;
    private double locationFromY;
    private double locationToX;
    private double locationToY;
    private float speedFrom;
    private float speedTo;


    public LatLng getLocationFrom() {
        if (locationFromX == 0.0 && locationFromY == 0.0) return null;
        return new LatLng(locationFromX,locationFromY) ;
    }

    public void setLocationFrom(LatLng locationFrom) {
        this.locationFromX = locationFrom.latitude;
        this.locationFromY = locationFrom.longitude;
    }
    public LatLng getLocationTo() {
        if (locationToX == 0.0 && locationToY == 0.0) return null;
        return new LatLng(locationToX,locationToY);
    }

    public void setLocationTo(LatLng locationTo) {
        this.locationToX = locationTo.latitude;
        this.locationToY = locationTo.longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Long getDateTo() {
        return dateTo;
    }

    public void setDateTo(Long dateTo) {
        this.dateTo = dateTo;
    }

    public double getLocationFromX() {
        return locationFromX;
    }

    public void setLocationFromX(double locationFromX) {
        this.locationFromX = locationFromX;
    }

    public double getLocationFromY() {
        return locationFromY;
    }

    public void setLocationFromY(double locationFromY) {
        this.locationFromY = locationFromY;
    }

    public double getLocationToX() {
        return locationToX;
    }

    public void setLocationToX(double locationToX) {
        this.locationToX = locationToX;
    }

    public double getLocationToY() {
        return locationToY;
    }

    public void setLocationToY(double locationToY) {
        this.locationToY = locationToY;
    }

    public float getSpeedFrom() {
        return speedFrom;
    }

    public void setSpeedFrom(float speedFrom) {
        this.speedFrom = speedFrom;
    }

    public float getSpeedTo() {
        return speedTo;
    }

    public void setSpeedTo(float speedTo) {
        this.speedTo = speedTo;
    }

    private UserLocation(UserLocationBuilder builder){
        id = builder.id;
        dateFrom = builder.dateFrom;
        dateTo = builder.dateTo;
        speedFrom = builder.speedFrom;
        if (builder.locationFrom != null){
            locationFromX = builder.locationFrom.latitude;
            locationFromY = builder.locationFrom.longitude;
        }
        if (builder.locationTo != null) {
            locationToX = builder.locationTo.latitude;
            locationToY = builder.locationTo.longitude;
        }
    }

    public UserLocation() {
    }

    public static class UserLocationBuilder{
        private Long id;
        private Long dateFrom;
        private Long dateTo;
        private LatLng locationFrom;
        private LatLng locationTo;
        private float speedFrom;
        private float speedTo;

        public UserLocationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserLocationBuilder dateFrom(Long dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public UserLocationBuilder dateTo(Long dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public UserLocationBuilder locationFrom(LatLng locationFrom) {
            this.locationFrom = locationFrom;
            return this;
        }

        public UserLocationBuilder locationTo(LatLng locationTo) {
            this.locationTo = locationTo;
            return this;
        }

        public UserLocationBuilder speedFrom(float speedFrom) {
            this.speedFrom = speedFrom;
            return this;
        }
        public UserLocationBuilder speedTo(float speedTo) {
            this.speedTo = speedTo;
            return this;
        }

        public UserLocation build(){
            return new UserLocation(this);
        }
    }
}
