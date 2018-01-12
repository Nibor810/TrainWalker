package com.example.robin.trainwalker;

/**
 * Created by Arthur on 16-12-2017.
 */

public class ApiController {

    private String username;
    private String password;

    public ApiController() {


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
    public void requestStationDepartureTime(String stationName) {

        new RequestStationDepartureTimes(stationName);
    }

    private class RequestStations implements AsyncResponse {

        public RequestStations() {

            String urlString = "http://webservices.ns.nl/ns-api-stations-v2";
            new RequestController(username, password, urlString, this).execute();
        }

        @Override
        public void processFinished(String result) {

            //TODO PARSE RESULT.
            System.out.println(result);
        }
    }

    private class RequestStationDepartureTimes implements AsyncResponse {

        public RequestStationDepartureTimes(String stationName) {

            String urlString = "https://webservices.ns.nl/ns-api-avt?station=" + stationName;
            new RequestController(username, password, urlString, this).execute();
        }

        @Override
        public void processFinished(String result) {

            //TODO PARSE RESULT.
            System.out.println(result);
        }
    }
}
