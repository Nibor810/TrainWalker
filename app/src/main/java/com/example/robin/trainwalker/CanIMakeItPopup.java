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
import android.view.ViewGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by robin on 12-1-2018.
 */

public class CanIMakeItPopup extends Dialog{
    Button cancel;
    Button accept;
    TextView textViewTime;
    PopUpCallBack callBack;
    ProgressBar progressBar;
    Station originStation;
    Station destinationStation;
    int travelTime;

    public CanIMakeItPopup(@NonNull Context context, PopUpCallBack callBack, int travelTime, Station originStation, Station destinationStation) {
        super(context);
        this.callBack = callBack;
        this.travelTime = travelTime;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
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
    }

    private void getTrainDepartureTime() {
        Date arrivalDate = calculateTrainDepartureTime();
        new DRApiController(new ResponseListener() {
            @Override
            public void getResult(Object object) {
                textViewTime.setText(getFirstPossibleDate((List<Train>) object,arrivalDate));
                ((ViewGroup)progressBar.getParent()).removeView(progressBar);
            }
        }).requestTravelOptions(originStation.getName(),destinationStation.getName());
    }

    private String getFirstPossibleDate(List<Train> trains,Date arrivalDate){
        for (Train train:trains) {
            Log.i("DATE", train.getDepartureTime()+" - "+train.getStartStation());
        }
        //TODO: Prioriteit: Hoog,  make sure dates is sorted for early to late
        //2018-01-14T23:10:00+0100
        if(!trains.isEmpty()) {
            List<Date> dates = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+0100'");
            for (Train train:trains) {
                Log.i("DATE", train.getDepartureTime());
            }

            for (Train train:trains) {
                Log.i("DATE", train.getDepartureTime());
                try {
                    Date date = format.parse(train.getDepartureTime());
                    Log.i("DATE",date.toString());
                    dates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            for (Date date : dates) {
                if (arrivalDate.before(date)) {
                    return date.toString();
                }
            }
        }
        return "There are no trains available";
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
