package com.akp.savarn.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.savarn.BottomSheetDialog;
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

public class MainActivity extends AppCompatActivity {
    RelativeLayout btn_login,mail_rl;
    EditText mob_et,pass_et;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    Button login_rl;
    TextView forget_tv,help_tv;
    private PopupWindow popupWindow1,popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        mob_et=findViewById(R.id.mob_et);
        btn_login=findViewById(R.id.btn_login);
        pass_et=findViewById(R.id.pass_et);
        login_rl=findViewById(R.id.login_rl);
        forget_tv=findViewById(R.id.forget_tv);
        help_tv=findViewById(R.id.help_tv);
        help_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDetailsAPI();
            }
        });
        mail_rl=findViewById(R.id.mail_rl);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else if (pass_et.getText().toString().equalsIgnoreCase("")){
                    pass_et.setError("Fields can't be blank!");
                    pass_et.requestFocus();
                }
                else {
                    mobileVerify();
                } }});
        forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget_password();
            }
        });
        login_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(Html.fromHtml("<font color='#FF7F27'>क्या आप सवर्ण है?</font>"));
                builder.setCancelable(false);
                builder.setNegativeButton("नही", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(),Register.class);
                        startActivity(intent);
                    }});
                AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                //Set negative button background
                nbutton.setBackgroundColor(Color.RED);
                //Set negative button text color
                nbutton.setTextColor(Color.WHITE);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                //Set positive button background
                pbutton.setBackgroundColor(Color.BLUE);
                //Set positive button text color
                pbutton.setTextColor(Color.WHITE);
            }
        });
    }
    public void mobileVerify() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"LoginWithUserIdPassowrd", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String statusa = object.getString("status");
                    if (statusa.equalsIgnoreCase("true")) {
//                    Toast.makeText(getApplicationContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("userid", jsonobject.getString("UserId"));
                            login_editor.putString("mobile", jsonobject.getString("MobileNo"));
                            login_editor.commit();
                            BottomSheetDialog bottomSheet = new BottomSheetDialog();
                            bottomSheet.show(getSupportFragmentManager(), "Full days");
                            Toast.makeText(getApplicationContext(), object.getString("Msg"), Toast.LENGTH_LONG).show();
                         /*   Intent intent = new Intent(getApplicationContext(), OTPVerify.class);
                            intent.putExtra("mobile_no", jsonobject.getString("MobileNo"));
                            intent.putExtra("code", jsonobject.getString("OTP"));
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), object.getString("Msg"), Toast.LENGTH_LONG).show();*/
                        }
                    }
                         else {
                            Toast.makeText(getApplicationContext(), object.getString("Msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", mob_et.getText().toString().trim());
                params.put("Password", pass_et.getText().toString().trim());
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

  /*  public  void  mobileVerify(){
        String otp1 = new GlobalAppApis().MobileNoVerifyA(mob_et.getText().toString());
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.MobileNoVerifyAPI(otp1);
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
                        Intent intent = new Intent(getApplicationContext(), OTPVerify.class);
                        intent.putExtra("mobile_no", jsonobject.getString("MobileNo"));
                        intent.putExtra("code", jsonobject.getString("OTP"));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, MainActivity.this, call1, "", true);
    }
*/

    public void forget_password(){
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.forget_password,null);
        ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
        EditText email_et = (EditText) customView.findViewById(R.id.email_et);
        AppCompatButton continue_btn = (AppCompatButton) customView.findViewById(R.id.continue_btn);
        //instantiate popup window
        popupWindow1 = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow1.showAtLocation(mail_rl, Gravity.BOTTOM, 0, 0);
        popupWindow1.setFocusable(true);
        // Settings disappear when you click somewhere else
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow1.update();
        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow1.dismiss();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming Soon!!\nContact to admin!",Toast.LENGTH_LONG).show();
//                FogetPassword(email_et.getText().toString());
            }
        });
    }

   /* private void FogetPassword(String email) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String statusa = object.getString("status");
                    if (statusa.equalsIgnoreCase("true")) {
//                    Toast.makeText(getApplicationContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), object.getString("Msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName",email);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }*/

    private void ViewDetailsAPI() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.viewdetails, null);
        ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow.showAtLocation(mail_rl, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        // Settings disappear when you click somewhere else
        popupWindow.setOutsideTouchable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.update();

        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}