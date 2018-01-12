package com.example.robin.trainwalker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

/**
 * Created by robin on 15-12-2017.
 */

public class FavoriteTrainPopupFragment extends Dialog {
    public Context context;
    public Button saveButton;
    public AutoCompleteTextView originStation;
    public AutoCompleteTextView destinationStation;
    public FavoriteTrainPopupFragment(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_favorite_train_popup);
        saveButton = findViewById(R.id.popup_favtrain_save);
        originStation = findViewById(R.id.ftPopup_textBoxBeginStation);
        destinationStation =findViewById(R.id.ftPopup_textBoxEndStation);
        saveButton.setOnClickListener(view -> {
            saveStations();
            dismiss();
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, new StationDBhelper(context).getAllStationNames());
        originStation.setThreshold(1);
        destinationStation.setThreshold(1);
        originStation.setAdapter(adapter);
        destinationStation.setAdapter(adapter);

    }

    private void saveStations() {
        saveWalkingSpeed("originStation",originStation.getText().toString());
        saveWalkingSpeed("destinationStation",destinationStation.getText().toString());
    }

    private void saveWalkingSpeed(String tag, String data) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(tag, data);
        editor.commit();
    }




}
