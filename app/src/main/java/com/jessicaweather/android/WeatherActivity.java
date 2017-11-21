package com.jessicaweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.jessicaweather.android.gson.Forecast;
import com.jessicaweather.android.gson.Weather;
import com.jessicaweather.android.service.AutoUpdateService;
import com.jessicaweather.android.util.HttpUtil;
import com.jessicaweather.android.util.Utility;

import org.litepal.util.BaseUtility;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    private String weatherCityName;

    public SwipeRefreshLayout swipeRefreshLayout;

    public DrawerLayout drawerLayout;

    private Button navButton;

    private AppBarLayout appBarLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);

        //init
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherString = prefs.getString("weather",null);
        final String weatherId;
        String bingPic = prefs.getString("bing_pic",null);

        if( bingPic != null ){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
        if(weatherString !=null ){
            //if cache exists read the cache
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            weatherCityName = weather.basic.cityName;
            showWeatherInfo(weather);
            //collapsingToolbarLayout.setTitle(weatherCityName);
        }else {
            // no cache , query service
            weatherId = getIntent().getStringExtra("weather_id");

            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
            //collapsingToolbarLayout.setTitle(weatherCityName);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                requestWeather(weatherId);
           }
        });

        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    /**
     * fix conflict of swipeRefreshLayout and AppBarLayout refresh
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout,int i){
        if(i == 0){
            swipeRefreshLayout.setEnabled(true);
        }else{
            swipeRefreshLayout.setEnabled(false);
        }
    }
    /**
     * fix conflict of swipeRefreshLayout and AppBarLayout refresh
     */
    @Override
    protected void onResume(){
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }
    /**
     * fix conflict of swipeRefreshLayout and AppBarLayout refresh
     */
    @Override
    protected void onPause(){
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    /**
     * query weather info by id
     */

    public void requestWeather(final String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=b43a7c722cdf4943a0b58fc34455397d";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather !=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                    WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        loadBingPic();
    }

    /**
     * load every day pic
     */
    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                        .edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 处理并展示weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){

        if(weather !=null && "ok".equals(weather.status)){
            String cityName = weather.basic.cityName;
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
            String degree = weather.now.temperature +"℃";
            String weatherInfo = weather.now.more.info;

            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            weatherInfoText.setText(weatherInfo);
            degreeText.setText(degree);
            forecastLayout.removeAllViews();
            for(Forecast forecast:weather.forecastList){
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_tiem,forecastLayout,false);
                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);
                ImageView weatherImage = (ImageView) view.findViewById(R.id.weather_info_image);

                switch (forecast.more.info){
                    case "多云":
                        weatherImage.setImageResource(R.drawable.mostly_cloudy_weather);
                        break;
                    case "晴":
                        weatherImage.setImageResource(R.drawable.sun);
                        break;
                    case "小雨":
                        weatherImage.setImageResource(R.drawable.raining_weather);
                        break;
                    case "少云":
                        weatherImage.setImageResource(R.drawable.mostly_sunny_weather_48px);
                        break;
                    case "晴间多云":
                        weatherImage.setImageResource(R.drawable.mostly_sunny_weather_48px);
                        break;
                    case "阴":
                        weatherImage.setImageResource(R.drawable.cloudy_weather);
                        break;
                    case "阵雨":
                        weatherImage.setImageResource(R.drawable.raining_weather);
                        break;
                    case "中雨":
                        weatherImage.setImageResource(R.drawable.raining_weather);
                        break;
                    case "大雨":
                        weatherImage.setImageResource(R.drawable.raining_weather);
                        break;
                    case "极端降雨":
                        weatherImage.setImageResource(R.drawable.raining_weather);
                        break;
                    case "小雪":
                        weatherImage.setImageResource(R.drawable.snowing_snow_weather);
                        break;
                    default:
                        weatherImage.setImageResource(R.drawable.cloudy_weather);
                        break;
                }

                dateText.setText("星期"+ getWeek(forecast.date));
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max + "°");
                minText.setText(forecast.temperature.min + "°");
                forecastLayout.addView(view);
            }
            if(weather.aqi != null){
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }
            String comfort = "舒适度" + weather.suggestion.comfort.info;
            String carWash = "洗车指数" +weather.suggestion.carWash.info;
            String sport = "运动建议" +weather.suggestion.sport.info;
            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);
            weatherLayout.setVisibility(View.VISIBLE);

            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else{
            Toast.makeText(WeatherActivity.this,"Access Weather info Failed",Toast.LENGTH_SHORT).show();
        }



    }

    /**
     * week show
     */
    private String getWeek(String date){
        String Week = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(format.parse(date));
        }catch (ParseException e){
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case 1:
                Week = "日";
                break;
            case 2:
                Week="一";
                break;
            case 3:
                Week="二";
                break;
            case 4:
                Week="三";
                break;
            case 5:
                Week="四";
                break;
            case 6:
                Week="五";
                break;
            case 7:
                Week="六";
                break;
            default:
                break;
        }
        return Week;
    }

}






















