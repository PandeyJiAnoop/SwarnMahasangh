package com.akp.savarn.footerpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.savarn.Basic.Api_Urls;
import com.akp.savarn.Basic.Register;
import com.akp.savarn.R;
import com.akp.savarn.Utility;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
ImageView back_img;
Button btn_signUp;
TextView et_Name,tv_mob_No;
TextInputEditText edt_name,edt_last,edt_email;

    private SharedPreferences sharedPreferences;
    String Userid;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    CircleImageView profile_image;

    String[] spinnercity = {"Select City"};
    String[] spinnerupline = {"Select Upline"};

    Spinner sp_state, sp_city;
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    ArrayList<String> CityName = new ArrayList<>();
    ArrayList<String> CityId = new ArrayList<>();
    String stateid, cityid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_edit_profile);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");
//        Toast.makeText(getApplicationContext(),Userid,Toast.LENGTH_LONG).show();
        back_img=findViewById(R.id.back_img);
        btn_signUp=findViewById(R.id.btn_signUp);
        et_Name=findViewById(R.id.et_Name);
        tv_mob_No=findViewById(R.id.tv_mob_No);
        edt_name=findViewById(R.id.edt_name);
        edt_last=findViewById(R.id.edt_last);
        edt_email=findViewById(R.id.edt_email);
        profile_image=findViewById(R.id.profile_image);
        sp_state = findViewById(R.id.sp_state);
        sp_city = findViewById(R.id.sp_city);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getState();
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        } }
                    CityName.clear();
                    getCity(stateid);
                }
                if (position == 0) {
                    CityName.clear();
                    ArrayAdapter<String> spinnerCity = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, spinnercity);
                    sp_city.setAdapter(spinnerCity);
                }
/*
                if (id == 0) {
                    CityName.clear();
                    ArrayAdapter<String> spinnerCity = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, spinnercity);
                    sp_city.setAdapter(spinnerCity);
                }
*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= CityId.size(); j++) {
                        if (sp_city.getSelectedItem().toString().equalsIgnoreCase(CityName.get(j))) {
                            cityid = CityId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        getcardDetails(Userid);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile_image.getDrawable() == null){
                    UpdateProfileAPI("");
                }
                else {
                    UpdateProfileAPI(temp);
                }



            }
        });




    }

    private void UpdateProfileAPI(String img) {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"UpdateProfile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(EditProfile.this,object.getString("Msg"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(EditProfile.this,object.getString("Msg"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",Userid);
                params.put("FirstName",edt_name.getText().toString());
                params.put("LastName",edt_last.getText().toString());
                params.put("EmailID",edt_email.getText().toString());
                params.put("ProfilePic",img);
                params.put("StateId", "35");
                params.put("DistrictId", "1047");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);

    }


    public void getcardDetails(String a_id) {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
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
                Log.d("test12345",jsonString);

                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Jarray = object.getJSONArray("UserResp");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            et_Name.setText(jsonobject.getString("UserName")+"("+jsonobject.getString("Caste")+")");
                            edt_name.setText(jsonobject.getString("FName"));
                            edt_last.setText(jsonobject.getString("LName"));
                            edt_email.setText(jsonobject.getString("Email"));
                            tv_mob_No.setText("Mobile No.+91-"+jsonobject.getString("MobileNo"));
                            if (jsonobject.getString("ProfilePhoto").equalsIgnoreCase("")){
                            }
                            else {
                                Glide.with(EditProfile.this).load(jsonobject.getString("ProfilePhoto")).into(profile_image);
                            }
                        }
                    }
                    else {
                        Toast.makeText(EditProfile.this,object.getString("Msg"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
    }







    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(EditProfile.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        profile_image.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }



    void getState() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetState", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    StateName.add("Select State");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId.add(jsonObject1.getString("StateId"));
                        String statename = jsonObject1.getString("State");
                        StateName.add(statename);
                    }
                    sp_state.setAdapter(new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    void getCity(final String stateid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetDistrict", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    CityName.add("Select City");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CityId.add(jsonObject1.getString("DistrictId"));
                        String Cityname = jsonObject1.getString("District");
                        CityName.add(Cityname);
                    }

                    sp_city.setAdapter(new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, CityName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
           /* public void onResponse(String response) {
                CityName.add("Select City");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            CityId.add(jsonObject1.getString("id"));
                            String Cityname = jsonObject1.getString("city");
                            CityName.add(Cityname);
                        }
                    } *//*else {
                        sp_city.setHint("No city available");
                        sp_city.setHintColor(getResources().getColor(R.color.black));
                    }*//*
                    sp_city.setAdapter(new ArrayAdapter<String>(VisitorLoginActivity.this, android.R.layout.simple_spinner_dropdown_item, CityName));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("StateId", stateid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}






