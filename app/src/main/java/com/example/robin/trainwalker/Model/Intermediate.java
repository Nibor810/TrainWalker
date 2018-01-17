package com.example.robin.trainwalker.Model;

/**
 * Created by Arthur on 17-1-2018.
 */

public class Intermediate {

    private String name;
    private String time;
    private String spoor;

    public Intermediate(String name, String time, String spoor) {

        this.name = name;
        this.time = time;
        this.spoor = spoor;
    }

    public String getName() {

        return name;
    }

    public String getTime() {

        return time;
    }

    public String getSpoor() {

        return spoor;
    }
}
