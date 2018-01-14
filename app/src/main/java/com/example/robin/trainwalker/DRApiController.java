package com.example.robin.trainwalker;

import android.util.Log;

/**
 * Created by Arthur on 16-12-2017.
 */

public class DRApiController
{
    private DRApiResponseParser drApiResponseParser;
    private String username;
    private String password;

    public DRApiController() {
        
        drApiResponseParser = new DRApiResponseParser();
    }

    public void requestStations() {

        new RequestStations();
    }

    /**
     * Request the departing trains of a given station.
     * The request will return at least 10 departing trains.
     * The request will return at least all departing trains of the coming hour.
     * This means that at a small station where a train stops every half hour, the departing trains-
     * for the coming five hours are returned to meet the minimum of 10 departing trains.
     * This also means that at a busy station where a train departes every 5 minutes, the return -
     * list contains 12 trains to meet the minimum of an hour of trains.
     * @param stationName
     */
    public void requestStationDepartingTrains(String stationName) {

        new RequestStationDepartingTrains(stationName);
    }

    /**
     *
     * @param stationName
     * @param planned
     * If you want to recieve the planned interruptions or not.
     * @param unplanned
     * If you want to recieve the unplanned interruptions or not.
     */
    public void requestStationInterruption(String stationName, boolean planned, boolean unplanned) {

        new RequestStationInterruption(stationName, planned, unplanned);
    }

    public void requestTravelOptions(String fromStation, String toStation) {

        new RequestTravelOptions(fromStation, toStation);
    }

    private class RequestStations implements AsyncResponse {

        public RequestStations() {

            String urlString = "http://webservices.ns.nl/ns-api-stations-v2";
            new RequestController(username, password, urlString, this).execute();
        }

        @Override
        public void processFinished(String result) {

            if(result != null)
                drApiResponseParser.parseStationRequest(result);
            else
                Log.d("ERROR", "Unable to request the list of railway stations.");
        }
    }

    private class RequestStationDepartingTrains implements AsyncResponse {

        private String stationName;

        public RequestStationDepartingTrains(String stationName) {

            this.stationName = stationName;
            String urlString = "https://webservices.ns.nl/ns-api-avt?station=" + stationName;
            new RequestController(username, password, urlString, this).execute();
        }

        @Override
        public void processFinished(String result) {

            if(result != null)
                drApiResponseParser.parseStationDepartingTrains(result, stationName);
            else
                Log.d("ERROR", "Unable to parse the list of departing trains");
        }
    }

    private class RequestStationInterruption implements AsyncResponse {

        public RequestStationInterruption(String stationName, boolean planned, boolean unplanned) {

            String urlString = "http://webservices.ns.nl/ns-api-storingen?station=" + stationName + "&actual=" + unplanned + "&unplanned=" + unplanned;
            new RequestController(username, password, urlString, this).execute();
        }

        @Override
        public void processFinished(String result) {

            //TODO PARSE RESULT.
            System.out.println(result);
        }
    }

    private class RequestTravelOptions implements AsyncResponse {

        private String fromStation;
        private String toStation;

        public RequestTravelOptions(String fromStation, String toStation) {

            this.fromStation = fromStation;
            this.toStation = toStation;

            String urlString = "https://webservices.ns.nl/ns-api-treinplanner?fromStation=" + fromStation + "&toStation=" + toStation;
            new RequestController(username, password, urlString, this).execute();
        }

        @Override
        public void processFinished(String result) {

            if(result != null)
                drApiResponseParser.parseTravelOptions(result, fromStation, toStation);
            else
                Log.d("ERROR", "Unable to parse the list of travel options.");
        }
    }
}
