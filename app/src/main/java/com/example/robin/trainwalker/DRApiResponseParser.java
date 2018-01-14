package com.example.robin.trainwalker;

import android.util.Log;

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
}
