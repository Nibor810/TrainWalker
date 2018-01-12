package com.example.robin.trainwalker;

/**
 * Created by Arthur on 16-12-2017.
 */

public class ApiController
{
    String username;
    String password;

    public ApiController()
    {
        username = "";
        password = "";
    }

    public void requestStations()
    {
        new RequestStations();
    }

    private class RequestStations implements AsyncResponse
    {
        public RequestStations()
        {
            String urlString = "http://webservices.ns.nl/ns-api-stations-v2";
            new RequestController(username, password, urlString, this).execute();
        }

        @Override
        public void processFinished(String result)
        {
            System.out.println(result);
        }
    }
}
