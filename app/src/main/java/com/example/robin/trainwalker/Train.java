package com.example.robin.trainwalker;

/**
 * Created by robin on 15-12-2017.
 */

class Train {
    private String startStation;
    private String endStation;
    private String departureTime;

    Train(String startStation, String endStation, String departureTime) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.departureTime = departureTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getName() {
        return startStation+" - "+endStation;
    }
}