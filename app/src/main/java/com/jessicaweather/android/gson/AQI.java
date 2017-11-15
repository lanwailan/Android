package com.jessicaweather.android.gson;

/**
 * Created by lanwailan on 2017/11/15.
 */

public class AQI {

    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
