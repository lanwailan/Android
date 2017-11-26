package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/26.
 */

public class Update {
    @SerializedName("loc")
    public String locTime;

    @SerializedName("utc")
    public String utcTime;
}
