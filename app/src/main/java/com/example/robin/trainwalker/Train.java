package com.example.robin.trainwalker;

/**
 * Created by robin on 15-12-2017.
 */

class Train {
    private String startStation;
    private String endStation;
    private String departureTime;
    private String trainType;
    private String departureTrack;

    Train(String startStation, String endStation, String departureTime, String trainType, String departureTrack) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.departureTime = departureTime;
        this.trainType = trainType;
        this.departureTrack = departureTrack;
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

    public String getTrainType() {
        return trainType;
    }

    public String getDepartureTrack() {
        return departureTrack;
    }

    public String getName() {
        return startStation+" - "+endStation;
    }

    @Override
    public String toString() {
        return "From: "+startStation+" To: "+endStation;
    }
}
