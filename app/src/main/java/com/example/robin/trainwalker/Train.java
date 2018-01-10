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

    Train(String startStation, String endStation){
        this.startStation = startStation;
        this.endStation = endStation;
        this.departureTime = "-";
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

    @Override
    public String toString() {
        return "From: "+startStation+" To: "+endStation;
    }
}
