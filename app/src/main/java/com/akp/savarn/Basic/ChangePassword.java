package com.akp.savarn.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akp.savarn.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText edt_new_pass;
    private EditText edt_old_pass, edt_conf_pass;
    private Button btn_sendotp;
    private SharedPreferences sharedPreferences;
    String Userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_change_password);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");

        edt_new_pass = (EditText) findViewById(R.id.edt_new_pass);
        edt_old_pass = (EditText) findViewById(R.id.edt_old_pass);
        edt_conf_pass = (EditText) findViewById(R.id.edt_conf_pass);
        btn_sendotp = (Button) findViewById(R.id.btn_sendotp);

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_old_pass.getText().toString().equalsIgnoreCase("")) {
                    edt_old_pass.setError("Fields can't be blank!");
                    edt_old_pass.requestFocus();
                } else if (edt_new_pass.getText().toString().equalsIgnoreCase("")) {
                    edt_new_pass.setError("Fields can't be blank!");
                    edt_new_pass.requestFocus();
                } else if (edt_conf_pass.getText().toString().equalsIgnoreCase("")) {
                    edt_conf_pass.setError("Fields can't be blank!");
                    edt_conf_pass.requestFocus();
                }
                else if(!edt_new_pass.getText().toString().equals(edt_conf_pass.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password Not matched!", Toast.LENGTH_LONG).show();
                }
                else {
                    changePassword();
                } }});
    }

    public void changePassword() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ChangePassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(ChangePassword.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePassword.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ChangePassword.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", Userid);
                params.put("OldPassword", edt_old_pass.getText().toString().trim());
                params.put("NewPassword", edt_new_pass.getText().toString().trim());
                params.put("ConfirmPassword", edt_conf_pass.getText().toString().trim());
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ChangePassword.this);
        requestQueue.add(stringRequest);

    }
}