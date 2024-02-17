package com.akp.savarn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.savarn.Basic.Api_Urls;
import com.akp.savarn.Basic.FullImagePage;
import com.akp.savarn.Basic.NetworkConnectionHelper;
import com.akp.savarn.Basic.NotificationList;
import com.akp.savarn.Basic.SearchScreen;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {
    ImageView notification_img, search_img;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private AlertDialog alertDialog;
    RecyclerView tranding_rec;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
 SwipeRefreshLayout srl_refresh;

    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    RecyclerView wallet_histroy;
    private SharedPreferences sharedPreferences;
    String Userid,mobile;
    private DashboardAdapter customerListAdapter;
    ImageView txt_nodata;

    RecyclerView cust_chat_recyclerView1;
    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<>();
    private ChatListAdapter chatHistoryAdapter;
    private PopupWindow popupWindow;
    private EditText message_edit;
    public static AppCompatButton previousSeletedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashboard);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");
        mobile = sharedPreferences.getString("mobile", "");


        tranding_rec = findViewById(R.id.tranding_rec);
        wallet_histroy = findViewById(R.id.wallet_histroy);
        notification_img = findViewById(R.id.notification_img);
        search_img = findViewById(R.id.search_img);
        srl_refresh = findViewById(R.id.srl_refresh);
        txt_nodata=findViewById(R.id.txt_nodata);

        findViewById(R.id.pink_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupwindowwallet();
            }
        });
        notification_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationList.class);
                startActivity(intent);
            }
        });


        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(DashboardActivity.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false); }
                    }, 2000);
                } else {
                    Toast.makeText(DashboardActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchScreen.class);
                startActivity(intent);
            }
        });
        GetCategoryAPI();
        getHistory("");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
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
            return false; }
    };


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton(Html.fromHtml("<font color='#000000'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity(); // or finish();
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#000000'>No</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            } }
    }

    private void showpopupwindowwallet() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.donate_popup_window, viewGroup, false);
        ImageView img = (ImageView) dialogView.findViewById(R.id.img);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void GetCategoryAPI() {
//        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
//        progressDialog.setMessage("Loading");
//        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetPostCategory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
//                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("CategoryID", jsonObject1.getString("CategoryID"));
                        hm.put("CategoryName", jsonObject1.getString("CategoryName"));
                        arrayList.add(hm);
                    }
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DashboardActivity.this, 3);
                    CategoryAdapter customerListAdapter = new CategoryAdapter(DashboardActivity.this, arrayList);
                    tranding_rec.setLayoutManager(gridLayoutManager);
                    tranding_rec.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Action", "8");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }





    public void getHistory(String catid) {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "PostList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.d("what",response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String statusa = jsonObject.getString("status");
                    if (statusa.equalsIgnoreCase("true")) {
                        txt_nodata.setVisibility(View.GONE);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("UserResp");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("PostID", jsonObject1.getString("PostID"));
                            hm.put("MemberName", jsonObject1.getString("MemberName"));
                            hm.put("UserID", jsonObject1.getString("UserID"));
                            hm.put("PostDate", jsonObject1.getString("PostDate"));
                            hm.put("Dis", jsonObject1.getString("Dis"));
                            hm.put("ProfilePhoto", jsonObject1.getString("ProfilePhoto"));
                            hm.put("PostPhoto", jsonObject1.getString("PostPhoto"));
                            hm.put("IsLike", jsonObject1.getString("IsLike"));
                            hm.put("LikeCount", jsonObject1.getString("LikeCount"));
                            hm.put("ViewCount", jsonObject1.getString("ViewCount"));
                            hm.put("CommentCount", jsonObject1.getString("CommentCount"));
                            hm.put("CategoryName", jsonObject1.getString("CategoryName"));
                        hm.put("FileType", jsonObject1.getString("FileType"));
                        hm.put("VideoUrl", jsonObject1.getString("VideoUrl"));
                        hm.put("user_id", Userid);
                            arrayList1.add(hm);
                        }
                    }
                    else {
                        txt_nodata.setVisibility(View.VISIBLE);
//                        Toast.makeText(DashboardActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DashboardActivity.this, 1);
                    customerListAdapter = new DashboardAdapter(DashboardActivity.this, arrayList1);
                    wallet_histroy.setLayoutManager(gridLayoutManager);
                    wallet_histroy.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Category", catid);
                params.put("UserId", Userid);
                params.put("FriendUserId","");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }





    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList3;
        public CategoryAdapter(Context context, List<HashMap<String,String>> arrayList3) {
            this.arrayList3=arrayList3;
            this.context=context; }
        @NonNull
        @Override
        public CategoryAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_category, viewGroup, false);
            CategoryAdapter.VH viewHolder = new CategoryAdapter.VH(view);
            return viewHolder; }
        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.VH vh, int i) {
            vh.cat_name_tv.setText(arrayList3.get(i).get("CategoryName"));


            vh.cat_name_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     if (arrayList3.get(i).get("CategoryName").equalsIgnoreCase("Post")){
                vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.purple_200) );

                         if ( ( previousSeletedButton == null ) || ( previousSeletedButton == vh.cat_name_tv ) ) {
                             vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.purple_200) );
                         }
                         else{
                             previousSeletedButton.setBackgroundColor( ContextCompat.getColor( context, R.color.blue) );
                             vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.purple_200) );
                         }
                         previousSeletedButton = vh.cat_name_tv;
                         arrayList1.clear();
//                         customerListAdapter.notifyDataSetChanged();
                         getHistory(arrayList3.get(i).get("CategoryID"));
            }



            if (arrayList3.get(i).get("CategoryName").equalsIgnoreCase("Legal Cell")){
                vh.cat_name_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( ( previousSeletedButton == null ) || ( previousSeletedButton == vh.cat_name_tv ) ) {
                            vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.purple_200) );
                        }
                        else{
                            previousSeletedButton.setBackgroundColor( ContextCompat.getColor( context, R.color.blue) );
                            vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.purple_200) );
                        }
                        previousSeletedButton = vh.cat_name_tv;
                        Intent intent=new Intent(getApplicationContext(),LegalMemberList.class);
                        startActivity(intent);
                    }
                });

            }

            if (arrayList3.get(i).get("CategoryName").equalsIgnoreCase("Member")){
                vh.cat_name_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( ( previousSeletedButton == null ) || ( previousSeletedButton == vh.cat_name_tv ) ) {
                            vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.purple_200) );
                        }
                        else{
                            previousSeletedButton.setBackgroundColor( ContextCompat.getColor( context, R.color.blue) );
                            vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.purple_200) );
                        }
                        previousSeletedButton = vh.cat_name_tv;
                        Intent intent=new Intent(getApplicationContext(),CommunityList.class);
                        startActivity(intent);
                    }
                });
            }
                }
            });
        }
        @Override
        public int getItemCount() {
            return arrayList3.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            AppCompatButton cat_name_tv;
            public VH(@NonNull View itemView) {
                super(itemView);
                cat_name_tv=itemView.findViewById(R.id.cat_name_tv);
            }}}


























    public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;
        private int like = 0;

        public DashboardAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public DashboardAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_youtube_dash, viewGroup, false);
            DashboardAdapter.VH viewHolder = new DashboardAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DashboardAdapter.VH vh, final int i) {
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.title_tv.setText(arrayList.get(i).get("MemberName"));
            vh.des_tv1.setText(arrayList.get(i).get("Dis"));
            vh.des_tv.setText(arrayList.get(i).get("PostDate"));
            vh.commnet_count_tv.setText(" View " + arrayList.get(i).get("CommentCount") + " Comments");
            vh.user_name_tv.setText(arrayList.get(i).get("CategoryName"));
            vh.like_tv.setText("Useful " + arrayList.get(i).get("LikeCount"));
            vh.view_tv.setText(arrayList.get(i).get("ViewCount") + " View");

            if (arrayList.get(i).get("FileType").equalsIgnoreCase("Video")){
                vh.video_img.setVisibility(View.VISIBLE);
                vh.pots_img.setAlpha((float) 0.6);
                vh.pots_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewPost(arrayList.get(i).get("user_id"), arrayList.get(i).get("PostID"), arrayList.get(i).get("UserID"));
                        Intent intent = new Intent(context, CutsomeVideoPlayer.class);
                        intent.putExtra("Path", arrayList.get(i).get("VideoUrl"));
                        context.startActivity(intent);
                    }
                });
            }
            else {
                vh.pots_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewPost(arrayList.get(i).get("user_id"), arrayList.get(i).get("PostID"), arrayList.get(i).get("UserID"));
                        Intent intent = new Intent(context, FullImagePage.class);
                        intent.putExtra("Path", arrayList.get(i).get("PostPhoto"));
                        context.startActivity(intent);
                    }
                });
                vh.video_img.setVisibility(View.GONE);
                vh.pots_img.setAlpha((float) 0.9);
            }


            vh.more_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPost(arrayList.get(i).get("user_id"), arrayList.get(i).get("PostID"), arrayList.get(i).get("UserID"));
                    Intent intent = new Intent(context, FullImagePage.class);
                    intent.putExtra("Path", arrayList.get(i).get("PostPhoto"));
                    context.startActivity(intent);
                }
            });



            if (arrayList.get(i).get("IsLike").equalsIgnoreCase("1")) {
                vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_245));
            } else {
                vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24));
            }

            vh.like_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "LikeDislikePost", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                            jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                            jsonString = jsonString.replace("</string>", " ");
                            Log.d("test", jsonString);
                            try {
                                JSONObject object = new JSONObject(jsonString);
                                String status = object.getString("Status");
                                if (status.equalsIgnoreCase("true")) {
                                    if (object.getString("Msg").equalsIgnoreCase("Post Like Successfully.")){
                                        vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_245));
                                        vh.like_tv.setText("Useful " + object.getString("LikeCount"));
                                    }
                                    if (object.getString("Msg").equalsIgnoreCase("Remove Like Successfully.")){
                                        vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24));
                                        vh.like_tv.setText("Useful " + object.getString("LikeCount"));
                                    }
                                } else {
                                    Toast.makeText(context, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("UserName", arrayList.get(i).get("user_id"));
                            params.put("PostID", arrayList.get(i).get("PostID"));
                            params.put("FriendID", arrayList.get(i).get("UserID"));
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);



                /*    if (like == 0) {
                        vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24));
                        like++;
                        LikeDislikePost(arrayList.get(i).get("user_id"), arrayList.get(i).get("PostID"), arrayList.get(i).get("UserID"));
                    } else if (like == 1) {
                        vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_245));
                        like = 0;
                        LikeDislikePost(arrayList.get(i).get("user_id"), arrayList.get(i).get("PostID"), arrayList.get(i).get("UserID"));
                    }*/
                }
            });

            verifyStoragePermission(DashboardActivity.this);
            vh.share_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takeScreenShot(vh.pots_img);
                }
            });

            if (arrayList.get(i).get("PostPhoto").equalsIgnoreCase("")) {
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
            } else {
                Glide.with(context).load(arrayList.get(i).get("PostPhoto")).error(R.drawable.noimage).into(vh.pots_img);
            }

            if (arrayList.get(i).get("ProfilePhoto").equalsIgnoreCase("")) {
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
            } else {
                Glide.with(context).load(arrayList.get(i).get("ProfilePhoto")).error(R.drawable.noimage).into(vh.user_profile_img);
            }
            if (arrayList.get(i).get("ProfilePhoto").equalsIgnoreCase("")) {
            } else {
                Glide.with(context).load(arrayList.get(i).get("ProfilePhoto")).error(R.drawable.noimage).into(vh.comment_profile);
            }

            vh.add_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AddCommentAPI(arrayList.get(i).get("user_id"), arrayList.get(i).get("PostID"), arrayList.get(i).get("UserID"),vh.comment_et.getText().toString());
                }
            });









//            Comment Code Start here

            vh.comment_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.dynamic_comment, null);
                    popupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popupWindow.setFocusable(true);
                    // Settings disappear when you click somewhere else
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    popupWindow.showAtLocation(v, Gravity.BOTTOM
                            | Gravity.CENTER_VERTICAL, 0, 0);
                    ImageView Goback = (ImageView) view.findViewById(R.id.Goback);
                    cust_chat_recyclerView1 =(RecyclerView) view.findViewById(R.id.cust_chat_recyclerView1);
                    ImageView send_btn = (ImageView) view.findViewById(R.id.send_btn);
                    message_edit = (EditText) view.findViewById(R.id.message_edit);
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"ViewCommentListbyPostId", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                            jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                            jsonString = jsonString.replace("</string>", " ");
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray jsonArrayr = jsonObject.getJSONArray("CommentListResp");
                                for (int i = 0; i < jsonArrayr.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("ChId", jsonObject1.getString("CommentId"));
                                    hm.put("Comment", jsonObject1.getString("Comment"));
                                    hm.put("ondate", jsonObject1.getString("CommentDate"));
                                    hm.put("userId", jsonObject1.getString("UserId"));
                                    hm.put("UserName", jsonObject1.getString("MemberName"));
                                    hm.put("ProfilePic", jsonObject1.getString("ProfilePic"));
                                    arrayList2.add(hm);
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                                chatHistoryAdapter = new ChatListAdapter(context, arrayList2);
                                cust_chat_recyclerView1.setLayoutManager(gridLayoutManager);
                                cust_chat_recyclerView1.setAdapter(chatHistoryAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                            Log.d("myTag", "message:" + error);
                            Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("UserName", arrayList.get(i).get("user_id"));
                            params.put("PostID", arrayList.get(i).get("PostID"));
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);




                    send_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"AddComment", new  Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    String jsonString = response;
                                    jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                                    jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                                    jsonString = jsonString.replace("</string>"," ");
                                    try {
                                        JSONObject jsonObject = new JSONObject(jsonString);
                                        String msg = jsonObject.getString("Msg");
                                        message_edit.setText("");
                                        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                                        arrayList2.clear();
//                    cust_chat_recyclerView1.getAdapter().notifyDataSetChanged();
                                        final ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("Loading...");
                                        progressDialog.show();
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ViewCommentListbyPostId", new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressDialog.dismiss();
                                                String jsonString = response;
                                                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                                                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                                                jsonString = jsonString.replace("</string>", " ");
                                                try {
                                                    JSONObject jsonObject = new JSONObject(jsonString);
                                                    JSONArray jsonArrayr = jsonObject.getJSONArray("CommentListResp");
                                                    for (int i = 0; i < jsonArrayr.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                                        HashMap<String, String> hm = new HashMap<>();
                                                        hm.put("ChId", jsonObject1.getString("CommentId"));
                                                        hm.put("Comment", jsonObject1.getString("Comment"));
                                                        hm.put("ondate", jsonObject1.getString("CommentDate"));
                                                        hm.put("userId", jsonObject1.getString("UserId"));
                                                        hm.put("UserName", jsonObject1.getString("MemberName"));
                                                        hm.put("ProfilePic", jsonObject1.getString("ProfilePic"));
                                                        arrayList2.add(hm);
                                                    }
                                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                                                    chatHistoryAdapter = new ChatListAdapter(context, arrayList2);
                                                    cust_chat_recyclerView1.setLayoutManager(gridLayoutManager);
                                                    cust_chat_recyclerView1.setAdapter(chatHistoryAdapter);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                                                Log.d("myTag", "message:" + error);
                                                Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                HashMap<String, String> params = new HashMap<>();
                                                params.put("UserName", arrayList.get(i).get("user_id"));
                                                params.put("PostID", arrayList.get(i).get("PostID"));
                                                return params;
                                            }
                                        };
                                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                                        requestQueue.add(stringRequest);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                                    Log.d("myTag", "message:"+error);
                                    Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("UserName", arrayList.get(i).get("user_id"));
                                    params.put("PostID", arrayList.get(i).get("PostID"));
                                    params.put("FriendID", arrayList.get(i).get("UserID"));
                                    params.put("Comment",message_edit.getText().toString());
                                    Log.d("sdsdsd",""+params);
                                    return params;
                                }
                            };
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);//                        Toast.makeText(getApplicationContext(),""+message_edit.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    Goback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrayList2.clear();
                            cust_chat_recyclerView1.getAdapter().notifyDataSetChanged();
                            popupWindow.dismiss();
                        }
                    });
                }});


//            Comment Code end here




























        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView title_tv, des_tv, commnet_count_tv, user_name_tv, like_tv, view_tv, des_tv1;
            ImageView pots_img, like_img,more_img;
            CircleImageView user_profile_img, comment_profile;
            LinearLayout like_ll,share_ll,comment_ll;
            ImageView add_img,video_img;
            EditText comment_et;


            public VH(@NonNull View itemView) {
                super(itemView);
                title_tv = itemView.findViewById(R.id.title_tv);
                des_tv = itemView.findViewById(R.id.des_tv);
                des_tv1 = itemView.findViewById(R.id.des_tv1);
                user_name_tv = itemView.findViewById(R.id.user_name_tv);
                commnet_count_tv = itemView.findViewById(R.id.commnet_count_tv);
                like_tv = itemView.findViewById(R.id.like_tv);
                view_tv = itemView.findViewById(R.id.view_tv);
                user_profile_img = itemView.findViewById(R.id.user_profile_img);
                comment_profile = itemView.findViewById(R.id.comment_profile);
                pots_img = itemView.findViewById(R.id.pots_img);
                share_ll=itemView.findViewById(R.id.share_ll);
                like_img = itemView.findViewById(R.id.like_img);
                like_ll = itemView.findViewById(R.id.like_ll);
                comment_et = itemView.findViewById(R.id.comment_et);
                comment_ll = itemView.findViewById(R.id.comment_ll);

                add_img = itemView.findViewById(R.id.add_img);
                more_img = itemView.findViewById(R.id.more_img);

                video_img = itemView.findViewById(R.id.video_img);
            }
        }




        public void ViewPost(String username, String postid, String friendno) {
//            final ProgressDialog progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ViewPost", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
//                    progressDialog.dismiss();
                    String jsonString = response;
                    jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                    jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                    jsonString = jsonString.replace("</string>", " ");
                    Log.d("test", jsonString);
                    try {
                        JSONObject object = new JSONObject(jsonString);
                        String status = object.getString("Status");
                        if (status.equalsIgnoreCase("true")) {
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//                            overridePendingTransition(0, 0);
//                        Toast.makeText(context, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
                    Toast.makeText(context, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("UserName", username);
                    params.put("PostID", postid);
                    params.put("FriendID", friendno);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }

    }




    private void takeScreenShot(View view) {
        Date date = new Date();
        CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);

        try {
            File mainDir = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
            if (!mainDir.exists()) {
                boolean mkdir = mainDir.mkdir();
            }

            String path = mainDir + "/" + "TrendOceans" + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            shareScreenShot(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Share ScreenShot
    private void shareScreenShot(File imageFile) {

        Uri uri =  FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Download सवर्ण जोड़ो अभियान From PlayStore \n https://play.google.com/store/apps/details?id=com.akp.savarn");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    //Permissions Check

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    }


