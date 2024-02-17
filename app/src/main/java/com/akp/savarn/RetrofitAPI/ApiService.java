package com.akp.savarn.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    String TAG = "ApiService";

    @Headers("Content-Type: application/json")
    @POST("MobileNoVerify")
    Call<String> MobileNoVerifyAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OtpVerify")
    Call<String> OtpVerifyAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Registration")
    Call<String> RegistrationAPI(
            @Body String body);
}

