package com.jessicaweather.android.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jessicaweather.android.db.City;
import com.jessicaweather.android.db.Country;
import com.jessicaweather.android.db.Provice;
import com.jessicaweather.android.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by lanwailan on 2017/11/13.
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for (int i =0;i < allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Provice provice = new Provice();
                    provice.setProvinceName(provinceObject.getString("name"));
                    provice.setProvinceCode(provinceObject.getInt("id"));
                    provice.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i =0 ;i < allCities.length();i ++ ){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountryResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCountries = new JSONArray(response);
                for(int i = 0; i< allCountries.length(); i++){
                    JSONObject countryObject = allCountries.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(countryObject.getString("name"));
                    country.setWeatherId(countryObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON 数据解析成Weather实体类
     */

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}


























