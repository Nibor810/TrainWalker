package com.example.robin.trainwalker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by robin on 15-12-2017.
 */

public class FavoriteTrainPopupFragment extends Dialog {
    public Context context;
    public Button saveButton;
    public FavoriteTrainPopupFragment(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_favorite_train_popup);
        saveButton = findViewById(R.id.popup_favtrain_save);
        saveButton.setOnClickListener(view -> {
            dismiss();
        });
    }
}
