package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/15.
 */

public class Now {

    @SerializedName("cloud")
    public String nowCloud;

    @SerializedName("cond_code")
    public String nowWeatherCode;

    @SerializedName("cond_txt")
    public String nowWeatherText;

    @SerializedName("fl")
    public String feelTemp;

    @SerializedName("hum")
    public String nowHumidity;

    @SerializedName("pcpn")
    public String nowRainWeight;

    @SerializedName("pres")
    public String nowPressure;

    @SerializedName("tmp")
    public String nowTemp;

    @SerializedName("vis")
    public String nowVisibility;

    @SerializedName("wind_deg")
    public String windDeg;

    @SerializedName("wind_dir")
    public String windDir;

    @SerializedName("wind_sc")
    public String windSc;

    @SerializedName("wind_spd")
    public String windspd;
}
