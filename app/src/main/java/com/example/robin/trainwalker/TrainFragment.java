package com.example.robin.trainwalker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

public class TrainFragment extends Fragment {
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

    private List<Train> getTempTrains() {
        ArrayList<Train> list = new ArrayList<>();
        list.add(new Train("Dordrecht","Breda","12:30"));
        list.add(new Train("Dordrecht","Breda","13:30"));
        list.add(new Train("Dordrecht","Breda","14:30"));
        list.add(new Train("Dordrecht","Breda","15:30"));
        list.add(new Train("Dordrecht","Breda","16:30"));
        list.add(new Train("Dordrecht","Breda","17:30"));
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new MyTrainRecyclerViewAdapter(getTempTrains()));
        }
        return view;
    }
}
