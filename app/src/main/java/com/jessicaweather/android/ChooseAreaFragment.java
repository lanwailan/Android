package com.jessicaweather.android;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jessicaweather.android.db.City;
import com.jessicaweather.android.db.Country;
import com.jessicaweather.android.db.Provice;
import com.jessicaweather.android.gson.Weather;
import com.jessicaweather.android.util.HttpUtil;
import com.jessicaweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lanwailan on 2017/11/13.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTRY =2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Provice> provinceList;

    /**
     * city list
     */
    private List<City> cityList;

    /**
     * country list
     */
    private List<Country> countryList;

    /**
     * selected province
     */
    private Provice selectedProvince;

    /**
     * selected city
     */
    private City selectedCity;

    /**
     * current level
     */
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTRY){
                    String weatherId = countryList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(currentLevel == LEVEL_COUNTRY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * query all provinces, first from database,if don't have,then to service
     */

    private void queryProvinces(){
        titleText.setText("China");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Provice.class);
        if(provinceList.size() > 0){
            dataList.clear();
            for(Provice province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    /**
     * query all seleted province about it's city
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince
                .getId())).find(City.class);
        if(cityList.size() >0 ){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }

    /**
     * select
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countryList = DataSupport.where("cityid = ?",String.valueOf(selectedCity
                .getId())).find(Country.class);
        if(countryList.size() > 0 ){
            dataList.clear();
            for(Country country : countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTRY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" +provinceCode + "/" +cityCode;
            queryFromServer(address,"country");
        }
    }

    /**
     * according to location to find data from web server
     */
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"Failed load",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("country".equals(type)){
                    result = Utility.handleCountryResponse(responseText,selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("country".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * show progress dialog
     */
    private void showProgressDialog(){
        if(progressDialog == null ){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * close dialog
     */
    private void closeProgressDialog(){
        if(progressDialog != null ){
            progressDialog.dismiss();
        }
    }


}




























