package com.akp.savarn.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */

public class User {
    private String cityName;
    public User(String cityName) {
        this.cityName = cityName;

    }

    public String getCityName() {
        return cityName;
    }
}
