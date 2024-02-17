package com.akp.savarn.Basic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.savarn.DashboardActivity;
import com.akp.savarn.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class SplashScreen extends AppCompatActivity {
    long Delay = 5000;
    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 1500;
    LinearLayout ll_1, ll_2;
    int progresscount = 0;
    ImageView img_icon;
    String general = "general-setting";
    Context context;
    private SharedPreferences sharedPreferences;
    String userid;

    String versionName = "", versionCode = "";
    String TAG ="splash";
    String prodid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        context = this.getApplicationContext();
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        UpdateVersion();
//        checkLogin();

        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        img_icon = findViewById(R.id.ivsplash);
        img_icon.setAnimation(downtoup);



    }
    private void checkLogin() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progresscount == 100) {
                    /**/
                } else {
                    handler.postDelayed(this, 30);
                    progresscount++;
                }
            }
        }, 200);

        Timer RunSplash = new Timer();
        // Task to do when the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkConnectionHelper.isOnline(SplashScreen.this)) {
                    if (userid.equalsIgnoreCase("")) {
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    } else {
                            startActivity(new Intent(SplashScreen.this, DashboardActivity.class));

                    }
                } else {

                    Toast.makeText(SplashScreen.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }, SPLASH_TIME_OUT);
    }



    public void AlertVersion() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        tvMessage.setText(getString(R.string.newVersion));
        btnSubmit.setText(getString(R.string.update));

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // or finish();
              /*  if (code.equalsIgnoreCase("")){
                    Intent myIntent = new Intent(SplashScreen.this,WelcomeSlider.class);
                    startActivity(myIntent);
                }
                else {
                    Intent myIntent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(myIntent);
                }*/
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
    }

    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));

            /*
             */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void UpdateVersion() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetVersion", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");

                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jobject = Jarray.getJSONObject(i);
                            String UpdateVersion = jobject.getString("Currentversion");
//                            Toast.makeText(getApplicationContext(),""+UpdateVersion,Toast.LENGTH_LONG).show();
                            getVersionInfo();
                            if (versionName.equalsIgnoreCase(UpdateVersion)) {
                                checkLogin();
                            } else {
                                AlertVersion();
                                //checkLogin();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            /*    try {
                    JSONObject object = new JSONObject(jsonString);
                    String UpdateVersion = object.getString("Version");
//                        Toast.makeText(getApplicationContext(),""+UpdateVersion,Toast.LENGTH_LONG).show();
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("true"))
                        getVersionInfo();
                    {
                        if (versionName.equalsIgnoreCase(UpdateVersion)) {
                            checkLogin();
                        } else {
                            AlertVersion();
                            //checkLogin();
                        } }
                }catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}