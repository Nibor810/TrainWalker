package com.example.robin.trainwalker.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.robin.trainwalker.Model.Station;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robin on 10-1-2018.
 */

public class StationDBhelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String STATIONS_DATABASE_NAME = "stations";
    private static final String KEY_STATIONS_NAME = "name";
    private static final String KEY_STATIONS_LATITUDE = "latitude";
    private static final String KEY_STATIONS_LONGITUDE = "longitude";
    private static final String STATION_TABLE_CREATE =
            "CREATE TABLE "+ STATIONS_DATABASE_NAME + " ("+
                    KEY_STATIONS_LATITUDE + " REAL, " +
                    KEY_STATIONS_LONGITUDE + " REAL, "+
                    KEY_STATIONS_NAME + " TEXT, "+
                    "PRIMARY KEY (" +
                    KEY_STATIONS_NAME +
                    "))";

    public StationDBhelper(Context context) {
        super(context, STATIONS_DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void testDatabase(){
        for (Station station:getAllStations()) {
            Log.i("DATABASE", station.getName());
        }
        LatLng coord = new LatLng(0,0);
        insertStation(new Station("Sliedrecht",new LatLng(51.82926685,4.77835536)));
        insertStation(new Station("Sliedrecht Baanhoek",new LatLng(51.8290613,4.74273294)));
        insertStation(new Station("Berlijn3",coord));
        insertStation(new Station("Berlijn4",coord));
        insertStation(new Station("Berlijn5",coord));
        insertStation(new Station("Berlijn6",coord));

        Log.i("DATABASE",getStation("Berlijn").getName());

    }

    public ArrayList<Station> getAllStations() {
        ArrayList<Station> array_list = new ArrayList<Station>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ STATIONS_DATABASE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(new Station(
                    res.getString(res.getColumnIndex(KEY_STATIONS_NAME)),
                    new LatLng(res.getDouble(res.getColumnIndex(KEY_STATIONS_LATITUDE)),res.getDouble(res.getColumnIndex(KEY_STATIONS_LONGITUDE)))
            ));

            res.moveToNext();
        }
        return array_list;
    }

    public boolean insertStation(Station station){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATIONS_NAME, station.getName());
        contentValues.put(KEY_STATIONS_LATITUDE, station.getCoordinate().latitude);
        contentValues.put(KEY_STATIONS_LONGITUDE, station.getCoordinate().longitude);
        db.insert(STATIONS_DATABASE_NAME, null, contentValues);
        return true;
    }

    public Station getStation(String stationName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ STATIONS_DATABASE_NAME +" WHERE "+KEY_STATIONS_NAME+" = '"+stationName+"'", null );
        if(res.getCount()>0) {
            res.moveToFirst();
            return new Station(
                    res.getString(res.getColumnIndex(KEY_STATIONS_NAME)),
                    new LatLng(res.getDouble(res.getColumnIndex(KEY_STATIONS_LATITUDE)),res.getDouble(res.getColumnIndex(KEY_STATIONS_LONGITUDE)))
                    );
        }
        return new Station("",new LatLng(0,0));
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(STATION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //TODO: Prioriteit: Laag, wat als de database word geupdate. nu tijdelijk wordt de database gedropt.

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ STATIONS_DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<String> getAllStationNames() {
        ArrayList<String> list = new ArrayList<String>();
        for (Station station:getAllStations()) {
            list.add(station.getName());
        }
        return list;
    }

    public void addStations(List<Station> stations) {
        for (Station station:stations) {
            insertStation(station);
        }

    }
}
