package com.example.robin.trainwalker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by robin on 12-1-2018.
 */

public class CanIMakeItPopup extends Dialog {
    Button cancel;
    Button accept;
    TextView textViewTime;
    PopUpCallBack callBack;

    public CanIMakeItPopup(@NonNull Context context, PopUpCallBack callBack) {
        super(context);
        this.callBack = callBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_popup_can_i_make_it);
        cancel = findViewById(R.id.popup_makeit_cancelButton);
        accept = findViewById(R.id.popup_makeit_continueButton);
        textViewTime = findViewById(R.id.popup_makeit_textViewTime);
        cancel.setOnClickListener(view -> {
            dismiss();
        });
        accept.setOnClickListener(view -> {
            goToMapFragment();
        });
        //TODO: send a request to NS API and google maps api to see what train can be taken while walking.
    }

    private void goToMapFragment() {
        callBack.doAfterPopup();
        dismiss();
    }


}
