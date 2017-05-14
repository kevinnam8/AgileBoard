package com.kevinnam.agileboard.models;

/**
 * Created by kevin on 14/05/2017.
 */

public class Column {

    public final static String KEY_STARTING = "starting";
    public final static String KEY_DONE = "done";

    private String name;
    private String key;
    private int limitOfWIP;

    public Column(String name, String key, int limitOfWIP) {
        this.name = name;
        this.key = key;
        this.limitOfWIP =  limitOfWIP;
    }
}
