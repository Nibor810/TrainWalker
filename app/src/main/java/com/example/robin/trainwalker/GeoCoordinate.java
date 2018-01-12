package com.example.robin.trainwalker;

/**
 * Created by robin on 10-1-2018.
 */

class GeoCoordinate {
    private double latitude;
    private double longitude;

    GeoCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
