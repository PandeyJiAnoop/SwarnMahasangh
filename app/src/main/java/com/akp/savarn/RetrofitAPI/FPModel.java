package com.akp.savarn.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class FPModel {
    public APIInterface fpApi;
    private RestAdapter restAdapter;
    //    public static final String CRAZYBILLA_API = "http://office.a2hosted.com/myTreat/public/api/v1/";
//    public static final String CRAZYBILLA_API = "http://13.126.214.49/api/v1/";
    public static final String Savarn = "http://savarn.signaturesoftware.org/WebService1.asmx/";

    public static final String GOOGLE_API = "https://maps.google.com";
    public FPModel(Context context, String hostAddress) {
        changeServerAddress(context, hostAddress);
    }

    public void changeServerAddress(Context context, String hostAddress) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient();
        okClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okClient.setReadTimeout(60, TimeUnit.SECONDS);
        okClient.setWriteTimeout(60, TimeUnit.SECONDS);
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(hostAddress)
                .setConverter(new SuccessResponseObject())
                .setClient(new ConnectivityAwareUrlClient(context, new OkClient(okClient)))
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build();
        fpApi = restAdapter.create(APIInterface.class);

    }
}
