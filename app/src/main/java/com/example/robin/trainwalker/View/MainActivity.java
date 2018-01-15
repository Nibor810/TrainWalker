package com.example.robin.trainwalker.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.robin.trainwalker.Controller.DRApiController;
import com.example.robin.trainwalker.Model.Station;
import com.example.robin.trainwalker.R;
import com.example.robin.trainwalker.Controller.StationDBhelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.navigation_map:
                    selectedFragment = MapFragment.newInstance();
                    break;
                case R.id.navigation_trains:
                    selectedFragment = TrainFragment.newInstance();
                    break;
                case R.id.navigation_settings:
                    selectedFragment = SettingsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame,selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment selectedFragment = HomeFragment.newInstance();
        transaction.replace(R.id.main_frame,selectedFragment);
        transaction.commit();
        updateDatabase(selectedFragment);
    }

    private void updateDatabase(Fragment selectedFragment){
        if(needToUpdateDatabase()) {
            UpdatingDatabasePopup customDialog =new UpdatingDatabasePopup(selectedFragment.getContext());
            customDialog.show();
            new DRApiController(object -> {
                StationDBhelper db = new StationDBhelper(getApplicationContext());
                db.addStations((List<Station>) object);
                saveLatestStationGetDate(Calendar.getInstance().getTime());
                customDialog.doneWithDatabaseUpdate();
            }).requestStations();
        }
    }

    private boolean needToUpdateDatabase() {
        Date currentDate = Calendar.getInstance().getTime();
        Date lastDatePlusAWeek = new Date(getLatestStationGetDate().getTime()+(1000*60*60*24*7));
        if(currentDate.after(lastDatePlusAWeek)){
            return true;
        }
        return false;
    }

    private void saveLatestStationGetDate(Date date) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("LatestStationGetDate", String.valueOf(date.getTime()));
        editor.commit();
    }

    private Date getLatestStationGetDate(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MY_PREF",Context.MODE_PRIVATE);
        return new Date(Long.valueOf(sharedPref.getString("LatestStationGetDate","0")));
    }

}
