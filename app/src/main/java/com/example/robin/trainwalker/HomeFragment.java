package com.example.robin.trainwalker;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment implements PopUpCallBack {

    private BottomNavigationView navigation;
    private Button favoriteTrainButton;
    private Button differentTrainButton;

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
        if(hasFavoriteTrain())
            showPopup();
    }

    private void showPopup(){
        CanIMakeItPopup customDialog = new CanIMakeItPopup(this.getContext(),this);
        customDialog.show();
    }

    private boolean hasFavoriteTrain() {
        return true;
    }

    private void goToTrainFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame,TrainFragment.newInstance());
        transaction.commit();
        navigation.setSelectedItemId(R.id.navigation_trains);
    }

    private void goToMapFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame,MapFragment.newInstance());
        transaction.commit();
        navigation.setSelectedItemId(R.id.navigation_map);
    }

    @Override
    public void doAfterPopup() {
        goToMapFragment();
    }
}
