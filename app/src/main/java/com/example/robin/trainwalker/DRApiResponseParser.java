package com.example.robin.trainwalker;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Arthur on 12-1-2018.
 */

public class DRApiResponseParser {

    private XmlPullParser xmlPullParser;

    public DRApiResponseParser() {

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            xmlPullParser = factory.newPullParser();

        }
        catch (XmlPullParserException e) {

            e.printStackTrace();
        }
    }

    public List<Station> parseStationRequest(String responseString) {

        Log.d("MESSAGE", "Parsing Station Request");

        List<Station> stations = new ArrayList<>();

        try {

            xmlPullParser.setInput(new StringReader(responseString));
            int eventType = xmlPullParser.getEventType();
            int i = 0;

            while(eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG && xmlPullParser.getName().equals("Station")) {

                    String stationName = "";
                    double latitude = 0;
                    double longitude = 0;

                    while (!(eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equals("Station"))) {

                        if(eventType == XmlPullParser.START_TAG) {

                            //Loop through the names and retrieve the name with the tag "long - lang".
                            if(xmlPullParser.getName().equals("Namen")) {

                                while(!(eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equals("Namen"))) {

                                    if(eventType == XmlPullParser.START_TAG && xmlPullParser.getName().equals("Lang")) {

                                        //Skip to the content of the Lang tag.
                                        xmlPullParser.next();
                                        stationName = xmlPullParser.getText();
                                    }

                                    eventType = xmlPullParser.next();
                                }
                            }
                            else if(xmlPullParser.getName().equals("Lat")) {

                                //Skip to the content of the Lat tag.
                                xmlPullParser.next();
                                latitude = Double.parseDouble(xmlPullParser.getText());
                            }
                            else if(xmlPullParser.getName().equals("Lon")) {

                                //Skip to the content of the Lon tag.
                                xmlPullParser.next();
                                longitude = Double.parseDouble(xmlPullParser.getText());
                            }
                        }

                        eventType = xmlPullParser.next();
                    }

                    stations.add(new Station(stationName, new LatLng(latitude, longitude)));
                }

                eventType = xmlPullParser.next();
            }

        }
        catch (XmlPullParserException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        Log.i("DB","done met parsen");
        return stations;
    }

    public List<Train> parseStationDepartingTrains(String responseString, String startStation) {

        Log.d("MESSAGE", "Parsing station departure times request");

        List<Train> trains = new ArrayList<>();

        try {

            xmlPullParser.setInput(new StringReader(responseString));
            int eventType = xmlPullParser.getEventType();
            int i = 0;

            while(eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG && xmlPullParser.getName().equals("VertrekkendeTrein")) {

                    String endStation = "";
                    String departureTime = "";
                    String traintype = "";
                    String departureTrack = "";

                    while (!(eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equals("VertrekkendeTrein"))) {

                        if(eventType == XmlPullParser.START_TAG) {

                            //Loop through the names and retrieve the name with the tag "long - lang".
                            if(xmlPullParser.getName().equals("VertrekTijd")) {

                                //Skip to the content of the VertrekTijd tag.
                                xmlPullParser.next();
                                departureTime = xmlPullParser.getText();
                            }
                            else if(xmlPullParser.getName().equals("EindBestemming")) {

                                //Skip to the content of the EindBestemming tag.
                                xmlPullParser.next();
                                endStation = xmlPullParser.getText();
                            }
                            else if(xmlPullParser.getName().equals("TreinSoort")) {

                                //Skip to the content of the TreinSoort tag.
                                xmlPullParser.next();
                                traintype = xmlPullParser.getText();
                            }
                            else if(xmlPullParser.getName().equals("VertrekSpoor")) {

                                //Skip to the content of the VertrekSpoor tag.
                                xmlPullParser.next();
                                departureTrack = xmlPullParser.getText();
                            }
                        }

                        eventType = xmlPullParser.next();
                    }

                    trains.add(new Train(startStation, endStation, departureTime, traintype, departureTrack));
                }

                eventType = xmlPullParser.next();
            }

        }
        catch (XmlPullParserException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }

        return trains;
    }
}
