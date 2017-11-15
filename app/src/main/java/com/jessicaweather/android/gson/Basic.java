package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/15.
 */

/**
 * "basic":{
 *     "city":"Shanghai",
 *     "id":"CN101190401",
 *     "update":{
 *         "loc":"2016-08-09 21:58"
 *     }
 * }
 */
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
