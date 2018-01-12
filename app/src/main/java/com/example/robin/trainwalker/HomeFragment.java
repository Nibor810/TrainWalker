package com.example.robin.trainwalker;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    Button favoriteTrainButton;
    Button differentTrainButton;
    BottomNavigationView navigation;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        navigation = getActivity().findViewById(R.id.navigation);
        favoriteTrainButton = view.findViewById(R.id.home_button_favoriteTrain);
        differentTrainButton = view.findViewById(R.id.home_bottun_differentTrain);
        favoriteTrainButton.setOnClickListener(view1 -> {
            onFavoriteTrainButtonClicked();
        });
        differentTrainButton.setOnClickListener(view12 -> {
            onDifferentTrainButtonClicked();
        });
        return view;
    }

    private void onDifferentTrainButtonClicked() {
        goToTrainFragment();
    }

    private void onFavoriteTrainButtonClicked() {
        
    }

    private void goToTrainFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame,TrainFragment.newInstance());
        transaction.commit();
        navigation.setSelectedItemId(R.id.navigation_trains);
    }
}
