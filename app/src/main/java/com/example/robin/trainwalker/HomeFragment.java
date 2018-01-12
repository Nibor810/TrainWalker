package com.example.robin.trainwalker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    private DRApiController drapicontroller;
    private Button favoriteTrainButton;
    private Button otherTrainButton;

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

        drapicontroller = new DRApiController();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        favoriteTrainButton = view.findViewById(R.id.home_FavoriteTrainButton);
        favoriteTrainButton.setOnClickListener(v-> {

            drapicontroller.requestTravelAdvice("Breda", "Dordrecht");
        });

        otherTrainButton = view.findViewById(R.id.home_OtherTrainButton);
        otherTrainButton.setOnClickListener(v -> {

            
        });

        return view;
    }
}
