package com.example.robin.trainwalker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.Api;

public class HomeFragment extends Fragment {

    ApiController apiController;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiController = new ApiController();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button favoriteTrainButton = view.findViewById(R.id.settings_selectFavoriteTrainButton);

        favoriteTrainButton.setOnClickListener(v->
        {
            apiController.requestStations();
        });

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
