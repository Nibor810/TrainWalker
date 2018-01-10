package com.example.robin.trainwalker;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by robin on 10-1-2018.
 */

public class DBhelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trains";
    private static final String FAV_TABLE_NAME = "favorite";
    private static final String KEY_TRAIN_DESTINATION = "destination";
    private static final String KEY_TRAIN_ISFAVORITE = "favorite";
    private static final String KEY_TRAIN_ORIGIN = "origin";
    private static final String KEY_TRAIN_DEPARTURE_TIME = "time";
    private static final String FAV_TABLE_CREATE =
            "CREATE TABLE "+ FAV_TABLE_NAME + " ("+
                    KEY_TRAIN_ISFAVORITE + "INTEGER" +
                    KEY_TRAIN_DESTINATION + "TEXT PRIMARY KEY, "+
                    KEY_TRAIN_ORIGIN + "TEXT PRIMARY KEY, "+
                    KEY_TRAIN_DEPARTURE_TIME + "TEXT PRIMARY KEY)";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FAV_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO: wat als de database word geupdate. nu tijdelijk wordt de database gedropt.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(sqLiteDatabase);
    }

    public boolean insertTrain(Train train){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TRAIN_ISFAVORITE, 0);
        contentValues.put(KEY_TRAIN_DESTINATION, train.getEndStation());
        contentValues.put(KEY_TRAIN_ORIGIN, train.getStartStation());
        contentValues.put(KEY_TRAIN_DEPARTURE_TIME, train.getDepartureTime());
        db.insert(FAV_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateTrain(Train train, boolean isFavorite){
        if (turnOffOldFavoriteTrain()) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_TRAIN_ISFAVORITE, 0);
            db.update(FAV_TABLE_NAME, contentValues,
                KEY_TRAIN_DESTINATION+" = "+train.getEndStation()+" AND "+KEY_TRAIN_ORIGIN+" = "+train.getStartStation()+" AND "+KEY_TRAIN_DEPARTURE_TIME+" = "+train.getDepartureTime(), null);
            return true;
        }
        return false;
    }

    private boolean turnOffOldFavoriteTrain() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TRAIN_ISFAVORITE, 0);
        db.update(FAV_TABLE_NAME, contentValues,
                KEY_TRAIN_ISFAVORITE+" = " +1, null);
        return true;
    }

}
