package com.example.robin.trainwalker.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.robin.trainwalker.R;

/**
 * Created by robin on 15-1-2018.
 */

public class UpdatingDatabasePopup extends Dialog {
    ProgressBar progressBar;
    public UpdatingDatabasePopup(@NonNull Context context) {
        super(context);
    }

    /**
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("POPUP","create");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_popup_updating_database);
        progressBar = findViewById(R.id.popup_databaseUpdate_Progressbar);
        progressBar.setIndeterminate(true);
    }
    */

    public void doneWithDatabaseUpdate(){
        Log.i("POPUP","close");
        dismiss();
    }
}
