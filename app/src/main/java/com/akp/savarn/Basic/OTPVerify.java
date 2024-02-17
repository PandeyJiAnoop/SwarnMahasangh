package com.akp.savarn.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class OTPVerify extends AppCompatActivity {
    AppCompatButton btnSubmit;
    String get_mobile,code;
    EditText code_et;
    TextView code_tv;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_o_t_p_verify);
        get_mobile = getIntent().getStringExtra("mobile_no");
        code = getIntent().getStringExtra("code");
        btnSubmit = findViewById(R.id.btnSubmit);
        code_tv = findViewById(R.id.code_tv);
        code_et = findViewById(R.id.code_et);
        code_tv.setText("Enter the code that was send to your Registered Mobile number +91-" + get_mobile + "\n your code is:- (" + code + ")");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code_et.getText().toString().equalsIgnoreCase("")) {
                    code_et.setError("Fields can't be blank!");
                    code_et.requestFocus();
                } else {
                    mobileVerify();
                }
            }
        });


    }

   /* public  void  mobileVerify(){
        String otp1 = new GlobalAppApis().OtpVerifyA(get_mobile,code_et.getText().toString());
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.OtpVerifyAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                String jsonString = result;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);

                try {
                    JSONObject object = new JSONObject(jsonString);
                    Toast.makeText(getApplicationContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();
                    JSONArray Jarray = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonobject = Jarray.getJSONObject(i);
                        Intent intent = new Intent(getApplicationContext(), Register.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OTPVerify.this, call1, "", true);
    }*/




    public void mobileVerify() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"OtpVerify", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);

                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            if (jsonobject.getString("UserStatus").equalsIgnoreCase("True")) {
                                login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                                login_editor = login_preference.edit();
                                login_editor.putString("userid", jsonobject.getString("UserId"));
                                login_editor.putString("mobile", jsonobject.getString("MobileNo"));
                                login_editor.commit();
                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), object.getString("Msg"), Toast.LENGTH_LONG).show();

                            } else {
                                Intent intent = new Intent(getApplicationContext(), Register.class);
                                intent.putExtra("mobilenumber",get_mobile);
                                startActivity(intent);
                            }

                        }
                    }
                        else {
                            Toast.makeText(getApplicationContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();

                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(OTPVerify.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNo", get_mobile);
                params.put("OTP", code_et.getText().toString().trim());
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerify.this);
        requestQueue.add(stringRequest);
    }


}
