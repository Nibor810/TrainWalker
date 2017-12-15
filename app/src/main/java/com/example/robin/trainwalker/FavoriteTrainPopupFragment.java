package com.example.robin.trainwalker;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by robin on 15-12-2017.
 */

public class FavoriteTrainPopupFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_train_popup, container, false);
    }

    public static FavoriteTrainPopupFragment newInstance() {
        FavoriteTrainPopupFragment fragment = new FavoriteTrainPopupFragment();
        return fragment;
    }


}
