package com.example.robin.trainwalker.View;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.robin.trainwalker.Controller.DRApiController;
import com.example.robin.trainwalker.Model.Station;
import com.example.robin.trainwalker.Model.Train;
import com.example.robin.trainwalker.Controller.PopUpCallBack;
import com.example.robin.trainwalker.R;
import com.example.robin.trainwalker.Controller.ResponseListener;

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
    int travelTimeInMinutes;
    int distanceInMeters;

    public CanIMakeItPopup(@NonNull Context context, PopUpCallBack callBack, int travelDistanceInMeters, Station originStation, Station destinationStation) {
        super(context);
        this.callBack = callBack;
        this.distanceInMeters = travelDistanceInMeters;
        this.travelTimeInMinutes = getTravelTimeInMinutes(travelDistanceInMeters);
        Log.i("TIME","reistijd: "+travelTimeInMinutes);
        this.originStation = originStation;
        this.destinationStation = destinationStation;
    }

    private int getTravelTimeInMinutes(float distanceInMeters){
        float walkingSpeedInKMPU = Float.valueOf(getWalkingSpeed());
        Log.i("TIME","afstand: "+distanceInMeters);
        Log.i("TIME","walkingSpeed: "+walkingSpeedInKMPU);
        float timeInMinutes = ((distanceInMeters/1000)/walkingSpeedInKMPU)*60;
        Log.i("TIME","tijd: "+timeInMinutes);
        return Math.round(timeInMinutes);
    }

    private String getWalkingSpeed(){
        SharedPreferences sharedPref = getContext().getSharedPreferences("MY_PREF",Context.MODE_PRIVATE);
        return sharedPref.getString("WalkingSpeed","4.7");
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
        if(!trains.isEmpty()) {
            List<Date> dates = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+0100'");
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
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
                    return displayFormat.format(date);
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
        Date arrivalTime = new Date(currentTimeInMillis +(travelTimeInMinutes*60*1000));
        Log.i("TIME", arrivalTime.toString());
        return arrivalTime;
    }
}
