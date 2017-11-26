package com.jessicaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanwailan on 2017/11/26.
 *  "lifestyle": [
                {
                    "brf": "舒适",
                    "txt": "今天夜间不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。",
                    "type": "comf"
                },
 */

public class Lifestyle {

    @SerializedName("brf")
    public String briefSuggestion;

    @SerializedName("txt")
    public String suggestion;

    @SerializedName("type")
    public String typeSuggestion;
}
