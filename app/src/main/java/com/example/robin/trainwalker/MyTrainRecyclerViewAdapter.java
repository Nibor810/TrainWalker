package com.example.robin.trainwalker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class MyTrainRecyclerViewAdapter extends RecyclerView.Adapter<MyTrainRecyclerViewAdapter.TrainViewHolder> {

    private final List<Train> trains;

    public MyTrainRecyclerViewAdapter(List<Train> items) {
        trains = items;
    }

    @Override
    public TrainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_train,parent,false));
    }

    @Override
    public void onBindViewHolder(TrainViewHolder holder, int position) {
        Train train = trains.get(position);
        holder.trainName.setText(trains.get(position).getName());
        holder.departTimeText.setText(trains.get(position).getDepartureTime());
    }

    @Override
    public int getItemCount() {
        return trains.size();
    }

    public static class TrainViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView trainName;
        public final TextView departTimeText;

        public TrainViewHolder(View view) {
            super(view);
            mView = view;
            trainName = view.findViewById(R.id.row_train_nameText);
            departTimeText = view.findViewById(R.id.row_train_timeText);
        }
    }
}
