package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/26.
 * "hourly": [
                {
                    "cloud": "8",
                    "cond_code": "100",
                    "cond_txt": "晴",
                    "hum": "84",
                    "pop": "0",
                    "pres": "1018",
                    "time": "2017-10-27 01:00",
                    "tmp": "8",
                    "wind_deg": "49",
                    "wind_dir": "东北风",
                    "wind_sc": "微风",
                    "wind_spd": "2"
                },
 *
 */

public class Hourly {

    @SerializedName("cloud")
    public String cloud;

    @SerializedName("cond_code")
    public String cloudCode;

    @SerializedName("hum")
    public String humidity;

    @SerializedName("pop")
    public String pop;

    @SerializedName("pres")
    public String hourlyPressure;

    @SerializedName("time")
    public String hourlyTime;

    @SerializedName("wind_deg")
    public String windDeg;

    @SerializedName("wind_dir")
    public String windDir;

    @SerializedName("wind_sc")
    public String windSc;

    @SerializedName("wind_spd")
    public String wind_spd;
}















