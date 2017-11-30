package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lanwailan on 2017/11/15.
 */

public class Weather {


    public Basic basic;


    public Update update;


    public String status;


    public Now now;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;

    @SerializedName("air_now_city")
    public Aircondition aircondition;


}
