package com.akp.savarn.RetrofitAPI;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

/**
 * Created by Anoop pandey-9696381023.
 */


public class PrefrenceHandler {
    private static SharedPreferences preferences = null;
    private static PrefrenceHandler prefrenceHandler = null;
    private final String LOGIN_STATUS = "LOGIN_STATUS";

    private final String API_TOKEN = "api_token";

    private final String FCM_KEY = "fcm_key";


    public String getFCM_KEY() {
        return preferences.getString(FCM_KEY, "");
    }

    public void setFCM_KEY(String value) {
        preferences.edit().putString(FCM_KEY, value).commit();
    }


    public String getAPI_TOKEN() {
        return preferences.getString(API_TOKEN, "");
    }

    public void setAPI_TOKEN(String value) {
        preferences.edit().putString(API_TOKEN, value).commit();
    }

    private PrefrenceHandler(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    private PrefrenceHandler() {

    }

    public static PrefrenceHandler getPreferences(Context context) {
        if (prefrenceHandler == null) {
            prefrenceHandler = new PrefrenceHandler(context);
        }
        return prefrenceHandler;
    }

    public void putObjectValue(Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            Object value = field.get(object);
            if (value != null) {
                preferences.edit().putString(field.getName(), value.toString().trim()).commit();
            } else {
                preferences.edit().putString(field.getName(), "").commit();
            }
        }
    }


    public Object getObjectValue(Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.set(object, preferences.getString(field.getName(), ""));
        }
        return object;
    }


    public void putStringValue(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public String getStringValue(String key) {
        return preferences.getString(key, "");
    }

    public void setLogin() {
        preferences.edit().putString(LOGIN_STATUS, "Login").commit();
    }

    public void setLogout() {
//        preferences.edit().putString(LOGIN_STATUS, "Logout").commit();
        preferences.edit().clear().commit();
    }

    public boolean isLogin() {
        return preferences.getString(LOGIN_STATUS, "").equalsIgnoreCase("login");
    }
}
