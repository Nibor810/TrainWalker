package com.example.robin.trainwalker;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    public final static String KEY_ROUTE = "ROUTE";
    private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private final static float DEFAULT_ZOOM = 18f;
    private final static String KEY_LOCATION = "LOCATION";
    private final static String KEY_CAMERA_POSITION = "CAMERA_POSITION";
    private final static int ZOOM_THRESHOLD = 10;
    private boolean fresh = true;
    private Location lastKnownLocation = null;
    private CameraPosition cameraPosition = null;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng defaultLocation = new LatLng(0, 0);

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    private void createGoogleApi() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        createGoogleApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (fresh) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
            getLocationPermission();
        }
        updateLocationUI();
        getDeviceLocation();
        routeRequest(createRouteURL(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()),new LatLng(51.82926685,4.77835536)));
        //routeRequest(createRouteURL(new LatLng(51.82328574,4.77466464),new LatLng(51.82926685,4.77835536)));
    }

    private void getDeviceLocation() {
        try {
            if (hasLocationPermission()) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this.getActivity(), (task) -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (cameraPosition == null && lastKnownLocation != null)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        if (cameraPosition == null)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void getLocationPermission() {
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{(android.Manifest.permission.ACCESS_FINE_LOCATION)},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        try {
            if (hasLocationPermission()) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private String createRouteURL(LatLng ownLocation,LatLng stationLocation) {
        // Origin of route
        LatLng originLatLng;
        String str_origin;

        // Detination of route
        LatLng destLatLng;
        String str_dest;

        // Mode of transportation
        String trafficMode = "mode=walking";

        // Waypoints of route
        originLatLng = ownLocation;
        str_origin = "origin=" + originLatLng.latitude + "," + originLatLng.longitude;

        destLatLng = stationLocation;
        str_dest = "destination=" + destLatLng.latitude + "," + destLatLng.longitude;

        // Url building
        String parameters = str_origin + "&" + str_dest + "&" + trafficMode;

        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.i("URL",url);
        return url;
    }

    private void routeRequest(String url){
        VolleyManager.getInstance(this.getActivity()).JsonObjectRequest(Request.Method.GET, url, null, object -> {
            JSONObject response = (JSONObject) object;
            Log.i("Route",response.toString());
            RouteDataParser dataParser = new RouteDataParser();
            drawRoute(dataParser.parseRoutesInfo(response));
        });
    }

    private void drawRoute(List<List<LatLng>> route) {

        PolylineOptions lineOptions = new PolylineOptions();
        for (int i = 0; i < route.size(); i++) {
            List<LatLng> leg = route.get(i);
            Log.i("DRAW","Leg: "+i);
            lineOptions.addAll(leg);
        }
        lineOptions.width(10);
        lineOptions.color(Color.RED);
        Log.d("onPostExecute","onPostExecute lineoptions decoded");

        // Drawing polyline in the Google Map for the i-th route
        if(lineOptions != null) {
            map.addPolyline(lineOptions);
        }
        else {
            Log.d("onPostExecute","without Polylines drawn");
        }
    }


}
