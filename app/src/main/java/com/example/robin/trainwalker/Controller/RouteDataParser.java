package com.example.robin.trainwalker.Controller;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by robin on 12-1-2018.
 */

public class RouteDataParser {
    public List<List<LatLng>> parseRoutesInfo(JSONObject jObject) {
        List<List<LatLng>> routes = new ArrayList<>();
        int distanceInMeters = 0;
        int durationInSeconds = 0;
        JSONArray jRoutes;
        try {
            jRoutes = jObject.getJSONArray("routes");
            JSONArray legs = jRoutes.getJSONObject(0).getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            distanceInMeters = leg.getJSONObject("distance").getInt("value");
            durationInSeconds = leg.getJSONObject("duration").getInt("value");

            JSONArray steps = leg.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i++) {
                String polyline;
                polyline = (String) ((JSONObject) ((JSONObject) steps.get(i)).get("polyline")).get("points");
                List<LatLng> list = decodePoly(polyline);

                routes.add(list);
            }
    } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("PARSE",distanceInMeters + " &"+durationInSeconds);
        return routes;
    }


    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public int parseWalkingTimeInSeconds(JSONObject jObject) {
        List<List<LatLng>> routes = new ArrayList<>();
        int durationInSeconds = 0;
        JSONArray jRoutes;
        try {
            jRoutes = jObject.getJSONArray("routes");
            JSONArray legs = jRoutes.getJSONObject(0).getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            durationInSeconds = leg.getJSONObject("duration").getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return durationInSeconds;
    }

    public int parseWalkingDistanceInMeters(JSONObject jObject) {
        List<List<LatLng>> routes = new ArrayList<>();
        int distanceInMeters = 0;
        JSONArray jRoutes;
        try {
            jRoutes = jObject.getJSONArray("routes");
            JSONArray legs = jRoutes.getJSONObject(0).getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            distanceInMeters = leg.getJSONObject("distance").getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distanceInMeters;
    }
}
