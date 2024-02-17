package com.akp.savarn.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.akp.savarn.R;

public class FAQ extends AppCompatActivity {
    Context context;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);
        context=this.getApplicationContext();
        ConnectivityManager connectivityManager=(ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo  network=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected())
        {
            //Internet available
            mWebView = findViewById(R.id.activity_main_webview);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new MyWebViewClient());
            //REMOTE RESOURCE
            mWebView.loadUrl("https://savarn.in/contact.aspx");
            //   Log.e("treeview","http://panchjyotivihar.com/AssociateTreeviewapp.aspx?username="+preferences.get(Constants.USERID));
        }
        else if(network.isConnected())
        {
            //Internet available
            mWebView = findViewById(R.id.activity_main_webview);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new MyWebViewClient());
            //REMOTE RESOURCE
            mWebView.loadUrl("https://savarn.in/contact.aspx");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Network Is Not Available",Toast.LENGTH_LONG).show();
        }
        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");
    }
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}