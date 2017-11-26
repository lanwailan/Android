package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/15.
 */

public class Forecast {

    public String date;

    @SerializedName("cond_code_d")
    public String dayWeatherCode;

    @SerializedName("cond_code_n")
    public String nightWeatherCode;

    @SerializedName("cond_txt_d")
    public String dayWeatherText;

    @SerializedName("cond_txt_n")
    public String nightWeatherText;

    @SerializedName("hum")
    public String cityHumidity;

    @SerializedName("mr")
    public String moonRise;

    @SerializedName("ms")
    public String moonSet;

    @SerializedName("pcpn")
    public String rainWeight;

    @SerializedName("pop")
    public String rainPossibility;

    @SerializedName("pres")
    public String rainPressure;

    @SerializedName("sr")
    public String sunRise;

    @SerializedName("ss")
    public String sunSet;

    @SerializedName("tmp_max")
    public String maxTemp;

    @SerializedName("tmp_min")
    public String minTemp;

    @SerializedName("uv_index")
    public String uvIndex;

    @SerializedName("vis")
    public String eyeVisibility;

    @SerializedName("wind_deg")
    public String windDeg;

    @SerializedName("wind_dir")
    public String windDir;

    @SerializedName("wind_sc")
    public String windSc;

    @SerializedName("wind_spd")
    public String wind_spd;
}

























