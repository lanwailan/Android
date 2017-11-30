package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/30.
 */

public class Aircondition {

    @SerializedName("aqi")
    public String air_aqi;

    @SerializedName("co")
    public String air_co;

    @SerializedName("main")
    public String air_main;

    @SerializedName("no2")
    public String air_no2;

    @SerializedName("o3")
    public String air_o3;

    @SerializedName("pm10")
    public String air_pm10;

    @SerializedName("pm25")
    public String air_pm25;

    public String pub_time;

    @SerializedName("qlty")
    public String air_quality;

    @SerializedName("so2")
    public String air_so2;

}
