package com.example.robin.trainwalker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by robin on 12-1-2018.
 */

public class ChosenTrainSingleton {
    public static ChosenTrainSingleton chosenTrainSingleton;
    private String chosenOriginStation;

    private ChosenTrainSingleton(){

    }

    public static ChosenTrainSingleton getInstance(){
        if(chosenTrainSingleton==null)
            chosenTrainSingleton = new ChosenTrainSingleton();
        return chosenTrainSingleton;
    }

    public String getChosenOriginStation() {
        return chosenOriginStation;
    }

    public void setChosenOriginStation(String chosenOriginStation) {
        this.chosenOriginStation = chosenOriginStation;
    }

}
