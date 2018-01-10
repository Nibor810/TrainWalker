package com.example.robin.trainwalker;

/**
 * Created by robin on 10-1-2018.
 */

public class Station {
    private String name;
    private GeoCoordinate coordinate;

    public Station(String name, GeoCoordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public GeoCoordinate getCoordinate() {
        return coordinate;
    }


}
