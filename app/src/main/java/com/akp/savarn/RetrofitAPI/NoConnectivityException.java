package com.akp.savarn.RetrofitAPI;

/**
 * Created by Anoop pandey-9696381023.
 */

public class NoConnectivityException extends RuntimeException {

    protected final String reason;


    public NoConnectivityException(String message) {
        super(message);
        this.reason = message;
    }

    @Override
    public String getMessage() {
        return reason;
    }

    public String getReason() {
        return reason;
    }

}