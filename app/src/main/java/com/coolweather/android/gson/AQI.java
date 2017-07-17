package com.coolweather.android.gson;

/**
 * Created by gaojianlong on 2017/7/17.
 */

public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;
        public String pm25;
    }
}
