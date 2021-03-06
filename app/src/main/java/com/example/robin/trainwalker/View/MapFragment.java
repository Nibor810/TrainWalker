package com.example.robin.trainwalker.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.robin.trainwalker.Controller.ChosenTrainSingleton;
import com.example.robin.trainwalker.Controller.RouteDataParser;
import com.example.robin.trainwalker.Model.Station;
import com.example.robin.trainwalker.Controller.PopUpCallBack;
import com.example.robin.trainwalker.R;
import com.example.robin.trainwalker.Controller.StationDBhelper;
import com.example.robin.trainwalker.Controller.VolleyManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, PopUpCallBack {
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
    private LocationCallback locationCallback;
    private Station originStation = new Station("Sliedrecht",new LatLng(51.82926685,4.77835536));
    private Station destinationStation = new Station("Sliedrecht",new LatLng(51.82926685,4.77835536));
    List<List<LatLng>> route;


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
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                //TODO: Prioriteit: Laag, wat te doen als locatie verandert.

            }
        };
        originStation = getOriginStation();
        destinationStation = getDestinationStation();
        createGoogleApi();
    }

    private Station getOriginStation() {
        StationDBhelper db = new StationDBhelper(this.getContext());
        String station;
        if(ChosenTrainSingleton.getInstance().getChosenOriginStation() == null){
            SharedPreferences sharedPref = getContext().getSharedPreferences("MY_PREF",Context.MODE_PRIVATE);
            station = sharedPref.getString("originStation"," ");
            Log.i("CHOOSE","origin not Singleton");
        } else {
            station = ChosenTrainSingleton.getInstance().getChosenOriginStation();
            Log.i("CHOOSE","origin Singleton");
        }
        return db.getStation(station);
    }

    private Station getDestinationStation() {
        StationDBhelper db = new StationDBhelper(this.getContext());
        String station;
        if(ChosenTrainSingleton.getInstance().getChosenDestinationStation() == null){
            SharedPreferences sharedPref = getContext().getSharedPreferences("MY_PREF",Context.MODE_PRIVATE);
            station = sharedPref.getString("destinationStation"," ");
            Log.i("CHOOSE","destination not Singleton");
        } else {
            station = ChosenTrainSingleton.getInstance().getChosenDestinationStation();
            Log.i("CHOOSE","destination Singleton");
        }
        return db.getStation(station);
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
    public void onResume() {
        registerLocationUpdates();
        super.onResume();
    }

    @Override
    public void onPause() {
        deregisterLocationUpdates();
        super.onPause();
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
    }


    private void getDeviceLocation() {
        try {
            if (hasLocationPermission()) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this.getActivity(), (task) -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        routeRequest(createRouteURL(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()), originStation.getCoordinate()));
                        if (cameraPosition == null && lastKnownLocation != null)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        if (cameraPosition == null)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);

                        //TODO: Prioriteit: Midden, popup dat de route vanaf huidige locatie niet kan worden berekent

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
                lastKnownLocation = map.getMyLocation();
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private String createRouteURL(LatLng startLocation,LatLng destinationLocation) {
        String trafficMode = "mode=walking";
        String str_origin = "origin=" + startLocation.latitude + "," + startLocation.longitude;
        String str_dest = "destination=" + destinationLocation.latitude + "," + destinationLocation.longitude;
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
            //drawRoute();
            route = dataParser.parseRoutesInfo(response);
            showPopup(dataParser.parseWalkingDistanceInMeters(response));

            //TODO: Prioriteit: Laag, Route opslaan zodat bij het verlaten van de fragment de route niet opnieuw opgehaalt hoeft te worden.

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
        if(lineOptions != null) {
            map.addPolyline(lineOptions);
        }
    }


    @SuppressLint("MissingPermission")
    private void registerLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setSmallestDisplacement(10)
                .setMaxWaitTime(1000)
                .setFastestInterval(10000);

        if (hasLocationPermission() && locationCallback != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void deregisterLocationUpdates() {
        if (locationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void showPopup(int travelTime){
        CanIMakeItPopup customDialog =new CanIMakeItPopup(this.getContext(),this,travelTime, originStation,destinationStation);
        customDialog.show();
    }


    @Override
    public void doAfterPopup() {
        drawRoute(route);
    }
}
