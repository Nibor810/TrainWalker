package com.example.robin.trainwalker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by robin on 10-1-2018.
 */

public class DBhelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "trains";
    private static final String KEY_TRAIN_DESTINATION = "destination";
    private static final String KEY_TRAIN_ISFAVORITE = "favorite";
    private static final String KEY_TRAIN_ORIGIN = "origin";
    private static final String KEY_TRAIN_DEPARTURE_TIME = "time";
    private static final String FAV_TABLE_CREATE =
            "CREATE TABLE "+ DATABASE_NAME + " ("+
                    KEY_TRAIN_ISFAVORITE + " INTEGER, " +
                    KEY_TRAIN_DESTINATION + " TEXT, "+
                    KEY_TRAIN_ORIGIN + " TEXT, "+
                    KEY_TRAIN_DEPARTURE_TIME + " TEXT, " +
                    "PRIMARY KEY (" +
                    KEY_TRAIN_DESTINATION + ", " +
                    KEY_TRAIN_ORIGIN + ", " +
                    KEY_TRAIN_DEPARTURE_TIME +
                    "))";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void dataBaseTest(){
        for (Train train: getAllTrains()) {
            Log.i("DATABASE TEST",train.toString());
        }
        insertTrain(new Train("Berlijn", "Stalingrad","19:43"));
        insertTrain(new Train("Berlijn1", "Stalingrad1","19:44"));
        insertTrain(new Train("Berlijn2", "Stalingrad2","19:45"));
        insertTrain(new Train("Berlijn3", "Stalingrad3","19:46"));
        insertTrain(new Train("Berlijn4", "Stalingrad4","19:47"));

        for (Train train: getAllTrains()) {
            Log.i("DATABASE TEST",train.toString());
        }
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

    public ArrayList<Train> getAllTrains() {
        ArrayList<Train> array_list = new ArrayList<Train>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+DATABASE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(new Train(res.getString(res.getColumnIndex(KEY_TRAIN_ORIGIN)),res.getString(res.getColumnIndex(KEY_TRAIN_DESTINATION)),res.getString(res.getColumnIndex(KEY_TRAIN_DEPARTURE_TIME))));

            res.moveToNext();
        }
        return array_list;
    }


    public boolean insertTrain(Train train){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TRAIN_ISFAVORITE, 0);
        contentValues.put(KEY_TRAIN_DESTINATION, train.getEndStation());
        contentValues.put(KEY_TRAIN_ORIGIN, train.getStartStation());
        contentValues.put(KEY_TRAIN_DEPARTURE_TIME, train.getDepartureTime());
        db.insert(DATABASE_NAME, null, contentValues);
        return true;
    }

    public boolean updateTrain(Train train, boolean isFavorite){
        if (turnOffOldFavoriteTrain()) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_TRAIN_ISFAVORITE, 0);
            db.update(DATABASE_NAME, contentValues,
                KEY_TRAIN_DESTINATION+" = "+train.getEndStation()+" AND "+KEY_TRAIN_ORIGIN+" = "+train.getStartStation()+" AND "+KEY_TRAIN_DEPARTURE_TIME+" = "+train.getDepartureTime(), null);
            return true;
        }
        return false;
    }

    private boolean turnOffOldFavoriteTrain() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TRAIN_ISFAVORITE, 0);
        db.update(DATABASE_NAME, contentValues,
                KEY_TRAIN_ISFAVORITE+" = " +1, null);
        return true;
    }

}
