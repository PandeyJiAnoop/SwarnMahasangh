package com.akp.savarn.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlobalAppApis {
    public String MobileNoVerifyA(String mob) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MobileNo", mob);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String OtpVerifyA(String mob,String otp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MobileNo", mob);
            jsonObject.put("OTP", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


    public String RegisterA(String name,String lanme,String mob,String email,String cast,String state,String distric,String image) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FirstName", name);
            jsonObject.put("LastName", lanme);
            jsonObject.put("MobileNo", mob);
            jsonObject.put("EmailID", email);
            jsonObject.put("Caste", cast);
            jsonObject.put("StateId", state);
            jsonObject.put("DistrictId", distric);
            jsonObject.put("ProfilePic", image);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String hotelList(String cityid, String priceorder, String fromdate, String todate, String gusest, String appversion, String playstoreversion, String imie) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CityID", cityid);
            jsonObject.put("PriceOrder", priceorder);
            jsonObject.put("FromDate", fromdate);
            jsonObject.put("ToDate", todate);
            jsonObject.put("Guest", gusest);
            JSONObject consumerSegments = new JSONObject();
            JSONArray list = new JSONArray();
            jsonObject.put("Brand",list);
            JSONArray lists = new JSONArray();
            jsonObject.put("Locality", lists);
            jsonObject.put("APPVersion", appversion);
            jsonObject.put("PlayStoreVersion", playstoreversion);
            jsonObject.put("IMEI", imie);


//            jsonObject.put("CityID", "20");
//            jsonObject.put("PriceOrder", "asc");
//            jsonObject.put("FromDate", "13/06/2022");
//            jsonObject.put("ToDate", "14/07/2022");
//            jsonObject.put("Guest", "2");
//            JSONObject consumerSegments = new JSONObject();
//            JSONArray list = new JSONArray();
//            jsonObject.put("Brand",list);
//            JSONArray lists = new JSONArray();
//            jsonObject.put("Locality", lists);
//            jsonObject.put("APPVersion", "112");
//            jsonObject.put("PlayStoreVersion", "123");
//            jsonObject.put("IMEI", "123");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}






