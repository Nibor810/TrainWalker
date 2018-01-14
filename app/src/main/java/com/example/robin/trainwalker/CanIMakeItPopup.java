package com.example.robin.trainwalker;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by robin on 12-1-2018.
 */

public class CanIMakeItPopup extends Dialog{
    Button cancel;
    Button accept;
    TextView textViewTime;
    PopUpCallBack callBack;
    ProgressBar progressBar;
    int travelTime;
    
    public CanIMakeItPopup(@NonNull Context context, PopUpCallBack callBack, int travelTime) {
        super(context);
        this.callBack = callBack;
        this.travelTime = travelTime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_popup_can_i_make_it);
        cancel = findViewById(R.id.popup_makeit_cancelButton);
        accept = findViewById(R.id.popup_makeit_continueButton);
        progressBar = findViewById(R.id.popup_makeit_progressBar);
        textViewTime = findViewById(R.id.popup_makeit_textViewTime);
        cancel.setOnClickListener(view -> {
            dismiss();
        });
        accept.setOnClickListener(view -> {
            goToMapFragment();
        });
        progressBar.setIndeterminate(true);
        getTrainDepartureTime();
        //TODO: turn off progressbar and display time of train
    }

    private void getTrainDepartureTime() {

    }

    private void goToMapFragment() {
        callBack.doAfterPopup();
        dismiss();
    }

    private Date calculateTrainDepartureTime(){
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date arrivalTime = new Date(currentTimeInMillis +(travelTime*1000));
        return arrivalTime;
    }
}
