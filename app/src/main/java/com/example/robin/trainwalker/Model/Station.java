package com.example.robin.trainwalker.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by robin on 10-1-2018.
 */

public class Station {
    private String name;
    private LatLng coordinate;

    public Station(String name, LatLng coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }


}
