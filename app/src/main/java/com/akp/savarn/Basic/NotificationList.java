package com.akp.savarn.Basic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akp.savarn.DashboardActivity;
import com.akp.savarn.R;
import com.akp.savarn.UploadVideo;
import com.akp.savarn.footerpage.MemberList;
import com.akp.savarn.footerpage.ProfileScreen;
import com.akp.savarn.footerpage.SettingScreen;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationList extends AppCompatActivity {
    ImageView back_img;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String Userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_notification_list);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);


        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(NotificationList.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(NotificationList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        NotificationAPI();


    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intents1 = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intents1);
                    return true;
                case R.id.navigation_member:
                    Intent intent = new Intent(getApplicationContext(), MemberList.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_add:
                    Intent intent1 = new Intent(getApplicationContext(), UploadVideo.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_comunity:
                    Intent intents = new Intent(getApplicationContext(), SettingScreen.class);
                    startActivity(intents);
                    return true;
                case R.id.navigation_profile:
                    Intent inten = new Intent(getApplicationContext(), ProfileScreen.class);
                    inten.putExtra("type","profile");
                    startActivity(inten);
                    return true;
            }
            return false;
        }
    };


    public void NotificationAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(NotificationList.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"NotificationList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
//                Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);

                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = object.getJSONArray("NotificationResp");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("NotificationID", jsonObject1.getString("NotificationID"));
                            hm.put("NotificationDis", jsonObject1.getString("NotificationDis"));
                            arrayList1.add(hm);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(NotificationList.this, 1);
                        NotificationAdapter customerListAdapter = new NotificationAdapter(NotificationList.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    }
                    else {
                        Toast.makeText(NotificationList.this,object.getString("Msg"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(NotificationList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", Userid);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(NotificationList.this);
        requestQueue.add(stringRequest);
    }

}
