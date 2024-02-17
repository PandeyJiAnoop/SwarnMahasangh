package com.akp.savarn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.savarn.Basic.Api_Urls;
import com.akp.savarn.Basic.NetworkConnectionHelper;
import com.akp.savarn.footerpage.MemberList;
import com.akp.savarn.footerpage.ProfileScreen;
import com.akp.savarn.footerpage.SettingScreen;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityList extends AppCompatActivity {
    ImageView back_img;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String Userid;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private CommunityList.FriendsListAdapter pdfAdapTer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_community_list);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommunityList.this,DashboardActivity.class);
                startActivity(intent);            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);


        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(CommunityList.this)) {
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
                    Toast.makeText(CommunityList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        GetCommunityListAPI();


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


    public void GetCommunityListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetCommiteeMemberList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("CommiteeMemberResp");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("UserId", jsonObject.getString("UserId"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("RegDate", jsonObject.getString("RegDate"));
                            hashlist.put("MobileNo", jsonObject.getString("MobileNo"));
                            hashlist.put("ProfilePhoto", jsonObject.getString("ProfilePhoto"));
                            hashlist.put("EmailID", jsonObject.getString("EmailID"));
                            hashlist.put("Address", jsonObject.getString("Address"));
                            hashlist.put("Designation", jsonObject.getString("Designation"));

                            arrFriendsList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new CommunityList.FriendsListAdapter(CommunityList.this, arrFriendsList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    } else {
                        Toast.makeText(CommunityList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CommunityList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(CommunityList.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter extends RecyclerView.Adapter<CommunityList.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public CommunityList.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommunityList.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_allcommunity, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final CommunityList.FriendsList holder, final int position) {
            holder.tv1.setText(data.get(position).get("MemberName"));
            holder.tv3.setText("Id- "+data.get(position).get("UserId"));
            holder.tv5.setText(data.get(position).get("EmailID"));
            holder.tv2.setText("Joining Date:- "+data.get(position).get("RegDate"));
            holder.tv4.setText(data.get(position).get("Address"));
            Picasso.get().load(data.get(position).get("ProfilePhoto")).into(holder.user_profile_img);

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        ImageView user_profile_img;
        TextView tv1, tv2, tv3, tv4, follow_tv,tv5;

        public FriendsList(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);

            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
            follow_tv = itemView.findViewById(R.id.follow_tv);

            user_profile_img = itemView.findViewById(R.id.user_profile_img);

        }
    }
    }
