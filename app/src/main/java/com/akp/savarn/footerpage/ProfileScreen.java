package com.akp.savarn.footerpage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.savarn.Basic.Api_Urls;
import com.akp.savarn.BuildConfig;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileScreen extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView rcvCalls;
    GridLayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    String Userid,usernam;
    ImageView profile_img,user_image;
    TextView name_tv,car_no_tv,loca_tv,membersince_tv,person_name_tv,account_create_date_tv;
ImageButton download_ib,sharelink_ib;

LinearLayout parentView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    Bitmap bitmap;
    String ImagePath;
    Uri URI;

    TextView post_tv,followers_tv,following_tv;
    String getType,getID;

    CardView cardlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_profile_screen);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");

        getType=getIntent().getStringExtra("type");
        getID=getIntent().getStringExtra("userkiid");

        back_btn = findViewById(R.id.back_btn);

        rcvCalls = findViewById(R.id.cust_recyclerView);
//        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
//        rcvCalls.setLayoutManager(layoutManager);
//        CallsAdapter CallsAdapter = new CallsAdapter();
//        rcvCalls.setAdapter(CallsAdapter);


        post_tv = findViewById(R.id.post_tv);
        followers_tv = findViewById(R.id.followers_tv);
        following_tv = findViewById(R.id.following_tv);



        name_tv = findViewById(R.id.name_tv);
        car_no_tv = findViewById(R.id.car_no_tv);
        loca_tv = findViewById(R.id.loca_tv);
        membersince_tv = findViewById(R.id.membersince_tv);
        profile_img = findViewById(R.id.profile_img);
        person_name_tv=findViewById(R.id.person_name_tv);
        account_create_date_tv=findViewById(R.id.account_create_date_tv);
        user_image=findViewById(R.id.user_image);
        parentView=findViewById(R.id.parentView);
        download_ib=findViewById(R.id.download_ib);
        sharelink_ib=findViewById(R.id.sharelink_ib);
        cardlayout=findViewById(R.id.cardlayout);

        download_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = ScreenshotUtil.getInstance().takeScreenshotForView(parentView); // Take ScreenshotUtil for any view
                requestPermissionAndSave();
            }
        });

        verifyStoragePermission(ProfileScreen.this);
        sharelink_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenShot(parentView);
//                takeScreenShot(getWindow().getDecorView());
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        if (getType.equalsIgnoreCase("list")){
            cardlayout.setVisibility(View.GONE);
            GetProfileDetailsAPI(getID);
            FollowFollowingPostDetailsAPI(getID);
        }
        if (getType.equalsIgnoreCase("profile")){
            cardlayout.setVisibility(View.VISIBLE);
            getcardDetails(Userid);
            GetProfileDetailsAPI(Userid);
            FollowFollowingPostDetailsAPI(Userid);
        }


    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent inten = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(inten);
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

                    return true;
            }
            return false;
        }
    };




    public void getcardDetails(String a_id) {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileScreen.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetCardDetails", new Response.Listener<String>() {
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
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Jarray = object.getJSONArray("UserResp");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);

                            name_tv.setText(jsonobject.getString("UserName")+"("+jsonobject.getString("Caste")+")");
                            car_no_tv.setText("CardId- "+jsonobject.getString("UserID"));
                            loca_tv.setText(jsonobject.getString("State")+", "+jsonobject.getString("District"));
                            membersince_tv.setText("Joining- "+jsonobject.getString("JoinDate"));


                            if (jsonobject.getString("ProfilePhoto").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Glide.with(ProfileScreen.this).load(jsonobject.getString("ProfilePhoto")).error(R.drawable.noimage).into(profile_img);
                            }




                        }
                    }
                    else {
                        Toast.makeText(ProfileScreen.this,object.getString("Msg"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", a_id);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileScreen.this);
        requestQueue.add(stringRequest);
    }


    private void requestPermissionAndSave() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (bitmap != null) {
                            ImagePath = MediaStore.Images.Media.insertImage(
                                    getContentResolver(),
                                    bitmap,
                                    "demo_image",
                                    "demo_image"
                            );

                            URI = Uri.parse(ImagePath);

                            Toast.makeText(ProfileScreen.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
//                            String path = Environment.getExternalStorageDirectory().toString() + "/test.png";
//                            FileUtil.getInstance().storeBitmap(bitmap, path);
//                            Toast.makeText(activity, getString(R.string.toast_message_screenshot_success) + " " + path, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProfileScreen.this, getString(R.string.toast_message_screenshot), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Toast.makeText(ProfileScreen.this, getString(R.string.settings_message), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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




    public void GetProfileDetailsAPI(String aid) {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileScreen.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"ProfileView", new Response.Listener<String>() {
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
                        JSONArray jsonArrayr = object.getJSONArray("ConResponse");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("PostImage", jsonObject1.getString("PostImage"));
                            hm.put("ViewCount", jsonObject1.getString("ViewCount"));
                            arrayList1.add(hm);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProfileScreen.this, 2);
                        ProfileListAdapter customerListAdapter = new ProfileListAdapter(ProfileScreen.this, arrayList1);
                        rcvCalls.setLayoutManager(gridLayoutManager);
                        rcvCalls.setAdapter(customerListAdapter);
                    }
                    else {
                        Toast.makeText(ProfileScreen.this,object.getString("Msg"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", aid);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileScreen.this);
        requestQueue.add(stringRequest);
    }





    public void FollowFollowingPostDetailsAPI(String aid) {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileScreen.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"SuggesstionProfileView", new Response.Listener<String>() {
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

                        followers_tv.setText(object.getString("FollwersCount"));
                        following_tv.setText(object.getString("FollwingCount"));
                        post_tv.setText(object.getString("PostCount"));

                        person_name_tv.setText(object.getString("Name"));
                        account_create_date_tv.setText("Member Since\n"+object.getString("JoinDate"));


                        if (object.getString("ProfilePhoto").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Glide.with(ProfileScreen.this).load(object.getString("ProfilePhoto")).error(R.drawable.noimage).into(user_image);
                        }

                    }
                    else {
                        Toast.makeText(ProfileScreen.this,object.getString("Msg"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", aid);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileScreen.this);
        requestQueue.add(stringRequest);
    }

}

