package com.akp.savarn.footerpage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.savarn.Basic.Api_Urls;
import com.akp.savarn.DashboardActivity;
import com.akp.savarn.R;
import com.akp.savarn.UploadVideo;
import com.android.volley.AuthFailureError;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberList extends AppCompatActivity {
    AppCompatButton follow_btn, message_btn;
    int i = 0;
    RelativeLayout all_cmember_ll, all_member_ll;
    ImageView back_btn;
RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String Userid;
    RecyclerView rcvList1;
    private final ArrayList<HashMap<String, String>> arrFriendsList1 = new ArrayList<>();
    private FriendsListAdapter pdfAdapTer;
    private FriendsListAdapter1 pdfAdapTer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_member_list);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvList = findViewById(R.id.rcvList);
        rcvList1 = findViewById(R.id.rcvList1);
        follow_btn = findViewById(R.id.follow_btn);
        message_btn = findViewById(R.id.message_btn);
        all_member_ll = findViewById(R.id.all_member_ll);
        all_cmember_ll = findViewById(R.id.all_cmember_ll);
        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow_btn.setBackgroundResource(R.drawable.bg_btn);
                message_btn.setBackgroundResource(R.drawable.rectoval);
                all_cmember_ll.setVisibility(View.GONE);
                all_member_ll.setVisibility(View.VISIBLE);
                /*if (i == 0) {
                    follow_btn.setText("Following");
                    follow_btn.setBackgroundResource(R.drawable.bg_search);
                    i++;
                } else if (i == 1) {
                    follow_btn.setText("Follow");
                    follow_btn.setBackgroundResource(R.drawable.bg_btn);
                    i = 0;
                }*/
            }
        });
        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow_btn.setBackgroundResource(R.drawable.rectoval);
                message_btn.setBackgroundResource(R.drawable.bg_btn);

                all_cmember_ll.setVisibility(View.VISIBLE);
                all_member_ll.setVisibility(View.GONE);

            }
        });
        GetUserListAPI();
        GetUserListAPI1();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_member:

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


    public void GetUserListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetUserList", new Response.Listener<String>() {
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
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("UserResp");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("UserId", jsonObject.getString("UserId"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("RegDate", jsonObject.getString("RegDate"));
                            hashlist.put("MobileNo", jsonObject.getString("MobileNo"));

                            hashlist.put("ProfilePhoto", jsonObject.getString("ProfilePhoto"));
                            hashlist.put("Caste", jsonObject.getString("Caste"));
                            hashlist.put("EmailID", jsonObject.getString("EmailID"));
                            hashlist.put("StateName", jsonObject.getString("StateName"));

                            hashlist.put("DistrictName", jsonObject.getString("DistrictName"));
                            hashlist.put("RequestSatus", jsonObject.getString("RequestSatus"));

                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        Toast.makeText(MemberList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MemberList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", Userid);
                Log.v("addadada", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MemberList.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter extends RecyclerView.Adapter<MemberList.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public MemberList.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MemberList.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_allmember, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final MemberList.FriendsList holder, final int position) {
            holder.tv1.setText(data.get(position).get("MemberName"));
            holder.tv3.setText("Id- "+data.get(position).get("UserId"));
            holder.tv2.setText("Joining Date:- "+data.get(position).get("RegDate"));
            holder.tv4.setText(data.get(position).get("StateName")+" "+data.get(position).get("DistrictName"));
            Picasso.get().load(data.get(position).get("ProfilePhoto")).into(holder.user_profile_img);

            if (data.get(position).get("RequestSatus").equalsIgnoreCase("")){
                holder.follow_tv.setText("Follow");
                holder.follow_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddFriendAPI(Userid,data.get(position).get("UserId"));


                    }
                });
            }
            else {
                holder.follow_tv.setText(data.get(position).get("RequestSatus"));

            }


            if (data.get(position).get("RequestSatus").equalsIgnoreCase("RequestRecived")){
                holder.follow_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AccepetdFriendRequestAPI(Userid,data.get(position).get("UserId"));
                    }
                });
            }

            if (data.get(position).get("RequestSatus").equalsIgnoreCase("RequestSend")){
                holder.follow_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoveFriendRequestAPI(Userid,data.get(position).get("UserId"));
                    }
                });
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),ProfileScreen.class);
                    intent.putExtra("type","list");
                    intent.putExtra("userkiid",data.get(position).get("UserId"));
                    startActivity(intent);
                }
            });





        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        CircleImageView user_profile_img;
        TextView tv1,tv2,tv3,tv4,follow_tv;

        public FriendsList(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);

            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);
            follow_tv=itemView.findViewById(R.id.follow_tv);

            user_profile_img=itemView.findViewById(R.id.user_profile_img);

        }
    }









    public void GetUserListAPI1() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetFriendList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("UserResp");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("UserId", jsonObject.getString("UserId"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("RegDate", jsonObject.getString("RegDate"));
                            hashlist.put("MobileNo", jsonObject.getString("MobileNo"));

                            hashlist.put("ProfilePhoto", jsonObject.getString("ProfilePhoto"));
                            hashlist.put("Caste", jsonObject.getString("Caste"));
                            hashlist.put("EmailID", jsonObject.getString("EmailID"));
                            hashlist.put("StateName", jsonObject.getString("StateName"));

                            hashlist.put("DistrictName", jsonObject.getString("DistrictName"));
                            hashlist.put("RequestSatus", jsonObject.getString("RequestSatus"));

                            arrFriendsList1.add(hashlist);
                        }
                        GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer1 = new FriendsListAdapter1(getApplicationContext(), arrFriendsList1);
                        rcvList1.setLayoutManager(layoutManager1);
                        rcvList1.setAdapter(pdfAdapTer1);
                    } else {
//                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(MemberList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", Userid);
                Log.v("addadada", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MemberList.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter1 extends RecyclerView.Adapter<MemberList.FriendsList1> {
        ArrayList<HashMap<String, String>> data1 = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter1(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList1) {
            data1 = arrFriendsList1;
        }
        public MemberList.FriendsList1 onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MemberList.FriendsList1(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_comuniteemember, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final MemberList.FriendsList1 holder, final int position) {
            holder.title_tv.setText(data1.get(position).get("MemberName"));
            holder.user_name_tv.setText("Id- "+data1.get(position).get("UserId"));
            holder.joing_date_tv.setText("Joining Date:- "+data1.get(position).get("RegDate"));

            if (data1.get(position).get("RequestSatus").equalsIgnoreCase("")){
                holder.ststus_tv.setVisibility(View.GONE);

            }
            else {
                holder.ststus_tv.setText(data1.get(position).get("RequestSatus"));
                holder.ststus_tv.setVisibility(View.VISIBLE);
            }
            holder.des_tv.setText(data1.get(position).get("StateName")+","+data1.get(position).get("DistrictName"));
            Picasso.get().load(data1.get(position).get("ProfilePhoto")).into(holder.user_profile_img1);


            if (data1.get(position).get("RequestSatus").equalsIgnoreCase("Friend")){
                holder.ststus_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoveFriendRequestAPI1(Userid,data1.get(position).get("UserId"));
                    }
                });
            }

        }



        public int getItemCount() {
            return data1.size();
        }
    }
    public class FriendsList1 extends RecyclerView.ViewHolder {
        CircleImageView user_profile_img1;
        TextView title_tv,des_tv,user_name_tv,joing_date_tv,ststus_tv;

        public FriendsList1(View itemView) {
            super(itemView);
            title_tv=itemView.findViewById(R.id.title_tv);
            des_tv=itemView.findViewById(R.id.des_tv);

            user_name_tv=itemView.findViewById(R.id.user_name_tv);
            joing_date_tv=itemView.findViewById(R.id.joing_date_tv);

            ststus_tv=itemView.findViewById(R.id.ststus_tv);

            user_profile_img1=itemView.findViewById(R.id.user_profile_img1);
        }
    }






    public void AddFriendAPI(String username,String friendno) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"AddFriend", new Response.Listener<String>() {
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
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                        pdfAdapTer.notifyDataSetChanged();
                        rcvList.invalidate();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MemberList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("FriendNo", friendno);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MemberList.this);
        requestQueue.add(stringRequest);

    }







    public void AccepetdFriendRequestAPI(String username,String friendno) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"ConfirmFriendRequest", new Response.Listener<String>() {
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
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                        pdfAdapTer.notifyDataSetChanged();
                        rcvList.invalidate();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MemberList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("FriendNo", friendno);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MemberList.this);
        requestQueue.add(stringRequest);

    }




    public void RemoveFriendRequestAPI(String username,String friendno) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"DeleteFriendRequest", new Response.Listener<String>() {
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
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                        pdfAdapTer.notifyDataSetChanged();
                        rcvList.invalidate();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MemberList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("FriendNo", friendno);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MemberList.this);
        requestQueue.add(stringRequest);

    }




    public void RemoveFriendRequestAPI1(String username,String friendno) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"DeleteFriendRequest", new Response.Listener<String>() {
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
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                        pdfAdapTer1.notifyDataSetChanged();
                        rcvList1.invalidate();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(MemberList.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MemberList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("FriendNo", friendno);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MemberList.this);
        requestQueue.add(stringRequest);

    }


}