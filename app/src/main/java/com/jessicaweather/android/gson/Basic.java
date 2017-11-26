package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/15.
 */
public class Basic {

    @SerializedName("cid")
    public String weatherId;

    @SerializedName("location")
    public String cityName;

    @SerializedName("parent_city")
    public String parentCity;

    @SerializedName("admin_area")
    public String adminArea;

    @SerializedName("cnty")
    public String cityCnty;

    @SerializedName("lat")
    public String cityLat;

    @SerializedName("lon")
    public String cityLon;

    @SerializedName("tz")
    public String cityTz;


}
