package com.example.robin.trainwalker.View;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.robin.trainwalker.Controller.ChosenTrainSingleton;
import com.example.robin.trainwalker.R;
import com.example.robin.trainwalker.Controller.StationDBhelper;

public class TrainFragment extends Fragment{
    Button chooseStationsButton;
    BottomNavigationView navigation;
    AutoCompleteTextView originStation;
    AutoCompleteTextView destinationStation;

    public TrainFragment() {
    }

    public static TrainFragment newInstance() {
        TrainFragment fragment = new TrainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        chooseStationsButton = view.findViewById(R.id.train_chooseStationsButton);
        navigation = getActivity().findViewById(R.id.navigation);
        originStation = view.findViewById(R.id.train_autoCompleteBeginStation);
        destinationStation = view.findViewById(R.id.train_autoCompleteEndStation);
        chooseStationsButton.setOnClickListener(view1 -> {
            saveChosenTrain();
            goToMapFragment();
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, new StationDBhelper(this.getContext()).getAllStationNames());
        originStation.setThreshold(1);
        destinationStation.setThreshold(1);
        originStation.setAdapter(adapter);
        destinationStation.setAdapter(adapter);

        return view;
    }

    private void goToMapFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, MapFragment.newInstance());
        transaction.commit();
        navigation.setSelectedItemId(R.id.navigation_map);
    }

    private void saveChosenTrain() {
        ChosenTrainSingleton.getInstance().setChosenOriginStation(originStation.getText().toString());
        ChosenTrainSingleton.getInstance().setChosenDestinationStation(destinationStation.getText().toString());
    }
}
