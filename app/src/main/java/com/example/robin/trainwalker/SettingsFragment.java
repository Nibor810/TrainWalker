package com.example.robin.trainwalker;


import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SettingsFragment extends Fragment {
    Button openPopupButton;
    EditText walkspeedTextBox;
    Button saveWalkingSpeedButton;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        openPopupButton = view.findViewById(R.id.settings_selectFavoriteTrainButton);

        openPopupButton.setOnClickListener(v->{
            showPopup();
        });
        walkspeedTextBox = view.findViewById(R.id.settings_walkingSpeedNumberBox);
        saveWalkingSpeedButton = view.findViewById(R.id.settings_saveWalkingSpeedButton);
        saveWalkingSpeedButton.setOnClickListener(view1 -> {
            saveWalkingSpeed();
        });
        walkspeedTextBox.setText(getWalkingSpeed());
        return view;
    }

    private void saveWalkingSpeed() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("WalkingSpeed", walkspeedTextBox.getText().toString());
        editor.commit();
    }

    private String getWalkingSpeed(){
        SharedPreferences sharedPref = getContext().getSharedPreferences("MY_PREF",Context.MODE_PRIVATE);
        return sharedPref.getString("WalkingSpeed","5");
    }



    private void showPopup(){
        FavoriteTrainPopupFragment customDialog =new FavoriteTrainPopupFragment(this.getContext());
        customDialog .show();
    }
}
