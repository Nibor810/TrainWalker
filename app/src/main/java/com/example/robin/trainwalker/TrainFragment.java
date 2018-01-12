package com.example.robin.trainwalker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;
import java.util.List;

public class TrainFragment extends Fragment implements PopUpCallBack {
    Button chooseStationsButton;

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
        chooseStationsButton.setOnClickListener(view1 -> {
            showPopup();
        });
        return view;
    }

    private void showPopup(){
        CanIMakeItPopup customDialog =new CanIMakeItPopup(this.getContext(),this);
        customDialog.show();
    }

    private void goToMapFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame,MapFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void doAfterPopup() {
        goToMapFragment();
    }
}
