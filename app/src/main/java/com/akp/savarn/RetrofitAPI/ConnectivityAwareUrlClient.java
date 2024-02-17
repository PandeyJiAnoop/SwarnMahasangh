package com.akp.savarn.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */

import android.content.Context;

import java.io.IOException;

import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;


public class ConnectivityAwareUrlClient implements Client {

    private static final String TAG = "BaseRetrofitClient";


    private Context context;


    private Client wrappedClient;

    public ConnectivityAwareUrlClient(Context context, Client client) {
        this.context = context;
        this.wrappedClient = client;
    }

    @Override
    public Response execute(Request request) throws IOException {
        if (!ConnectivityUtil.isConnected(context)) {
            throw RetrofitError.unexpectedError("No internet", new NoConnectivityException("No Internet"));
        } else {
            return wrappedClient.execute(request);
        }
    }


}