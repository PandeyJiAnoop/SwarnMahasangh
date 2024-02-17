package com.akp.savarn.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class Register extends AppCompatActivity {
AppCompatButton btn_signUp;
//    private String selectedDistrict, selectedState;                 //vars to hold the values of selected State and District
//    private TextView  tvStateSpinner, tvDistrictSpinner;             //declaring TextView android:fontFamily="@font/times_new_roman" to show the errors
//    private Spinner stateSpinner, districtSpinner;                  //Spinners
//    private ArrayAdapter<CharSequence> stateAdapter, districtAdapter;   //declare adapters for the spinners

    String[] spinnercity = {"Select City"};
    String[] spinnerupline = {"Select Upline"};
    Spinner sp_state,
            sp_city;
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    ArrayList<String> CityName = new ArrayList<>();
    ArrayList<String> CityId = new ArrayList<>();
    String stateid, cityid;
    TextInputEditText fname_et,name_et,mobile_et,email_et,pasword_et,confirm_pass_et;
    CircleImageView profile_img;
    Spinner cast_sp;

    String[] spinnerGender = new String[]{
            "ब्राह्मण",
            "क्षत्रिय",
            "बैश्य",
            "कायस्थ",
            "भूमिहार",
            "त्यागी",
    };
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    String getMobile;
    CheckBox cbWindows;
    TextView view_details_tv;
    private PopupWindow popupWindow,popupWindow2;
    RelativeLayout main_rl;
    LinearLayout login_rl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_register);

        getMobile=getIntent().getStringExtra("mobilenumber");

        btn_signUp=findViewById(R.id.btn_signUp);
        sp_state = findViewById(R.id.sp_state);
        sp_city = findViewById(R.id.sp_city);
        view_details_tv=findViewById(R.id.view_details_tv);
        main_rl=findViewById(R.id.main_rl);

        login_rl=findViewById(R.id.login_rl);


        fname_et=findViewById(R.id.fname_et);
        name_et = findViewById(R.id.name_et);
        mobile_et = findViewById(R.id.mobile_et);
        email_et=findViewById(R.id.email_et);
        profile_img = findViewById(R.id.profile_img);
        cast_sp = findViewById(R.id.cast_sp);
        pasword_et=findViewById(R.id.password_et);
        confirm_pass_et=findViewById(R.id.conf_pasword_et);
        mobile_et.setText(getMobile);
        cbWindows=findViewById(R.id.cbWindows);
        ArrayAdapter<String> spinnerArrayAdapterGender = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerGender);
        spinnerArrayAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_divider);
        cast_sp.setAdapter(spinnerArrayAdapterGender);


        view_details_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ViewDetailsAPI();
            }
        });


        login_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =new Intent(getApplicationContext(),MainActivity.class);
               startActivity(intent);
            }
        });



        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fname_et.getText().toString().equals("")){
                    fname_et.setError("Fields can't be blank!");
                    fname_et.requestFocus();
                }
                else if (name_et.getText().toString().equals("")){
                    name_et.setError("Fields can't be blank!");
                    name_et.requestFocus();
                }
                else if (mobile_et.getText().toString().equals("")){
                    mobile_et.setError("Fields can't be blank!");
                    mobile_et.requestFocus();
                }
            /*    else if (email_et.getText().toString().equals("")){
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                }
                else if (profile_img.getDrawable() == null){
                    Toast.makeText(getApplicationContext(),"Upload Your Profile Image for Generate Card",Toast.LENGTH_LONG).show();
                }*/
                else if (pasword_et.getText().toString().equalsIgnoreCase("")){
                    pasword_et.setError("Fields can't be blank!");
                    pasword_et.requestFocus();
                }
                else  if (confirm_pass_et.getText().toString().equalsIgnoreCase("")){
                    confirm_pass_et.setError("Fields can't be blank!");
                    confirm_pass_et.requestFocus();
                }
                else if(!pasword_et.getText().toString().equals(confirm_pass_et.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password Not matched!", Toast.LENGTH_LONG).show();
                }
                else if(!cbWindows.isChecked()){
                    cbWindows.requestFocus();
                    Toast.makeText(getApplicationContext(),"Accept our terms and conditions!",Toast.LENGTH_LONG).show();
                    //do some validation
                }
                else {
                    RegisterA(fname_et.getText().toString(),name_et.getText().toString(),mobile_et.getText().toString(),"","","","","");
                }
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
                    ArrayAdapter<String> spinnerCity = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, spinnercity);
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

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


     /*   //State Spinner Initialisation
        stateSpinner = findViewById(R.id.spinner_indian_states);    //Finds a view that was identified by the android:id attribute in xml

        //Populate ArrayAdapter using string array and a spinner layout that we will define
        stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_indian_states, R.layout.spinner_layout);

        // Specify the layout to use when the list of choices appear
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(stateAdapter);            //Set the adapter to the spinner to populate the State Spinner

        //When any item of the stateSpinner uis selected
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Define City Spinner but we will populate the options through the selected state
                districtSpinner = findViewById(R.id.spinner_indian_districts);

                selectedState = stateSpinner.getSelectedItem().toString();      //Obtain the selected State

                int parentID = parent.getId();
                if (parentID == R.id.spinner_indian_states){
                    switch (selectedState){
                        case "Select Your State": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_default_districts, R.layout.spinner_layout);
                            break;
                        case "Andhra Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_andhra_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Arunachal Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Assam": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_assam_districts, R.layout.spinner_layout);
                            break;
                        case "Bihar": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_bihar_districts, R.layout.spinner_layout);
                            break;
                        case "Chhattisgarh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_chhattisgarh_districts, R.layout.spinner_layout);
                            break;
                        case "Goa": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_goa_districts, R.layout.spinner_layout);
                            break;
                        case "Gujarat": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_gujarat_districts, R.layout.spinner_layout);
                            break;
                        case "Haryana": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_haryana_districts, R.layout.spinner_layout);
                            break;
                        case "Himachal Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_himachal_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Jharkhand": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_jharkhand_districts, R.layout.spinner_layout);
                            break;
                        case "Karnataka": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_karnataka_districts, R.layout.spinner_layout);
                            break;
                        case "Kerala": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_kerala_districts, R.layout.spinner_layout);
                            break;
                        case "Madhya Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_madhya_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Maharashtra": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_maharashtra_districts, R.layout.spinner_layout);
                            break;
                        case "Manipur": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_manipur_districts, R.layout.spinner_layout);
                            break;
                        case "Meghalaya": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_meghalaya_districts, R.layout.spinner_layout);
                            break;
                        case "Mizoram": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_mizoram_districts, R.layout.spinner_layout);
                            break;
                        case "Nagaland": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_nagaland_districts, R.layout.spinner_layout);
                            break;
                        case "Odisha": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_odisha_districts, R.layout.spinner_layout);
                            break;
                        case "Punjab": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_punjab_districts, R.layout.spinner_layout);
                            break;
                        case "Rajasthan": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_rajasthan_districts, R.layout.spinner_layout);
                            break;
                        case "Sikkim": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_sikkim_districts, R.layout.spinner_layout);
                            break;
                        case "Tamil Nadu": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_tamil_nadu_districts, R.layout.spinner_layout);
                            break;
                        case "Telangana": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_telangana_districts, R.layout.spinner_layout);
                            break;
                        case "Tripura": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_tripura_districts, R.layout.spinner_layout);
                            break;
                        case "Uttar Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_uttar_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Uttarakhand": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_uttarakhand_districts, R.layout.spinner_layout);
                            break;
                        case "West Bengal": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_west_bengal_districts, R.layout.spinner_layout);
                            break;
                        case "Andaman and Nicobar Islands": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_andaman_nicobar_districts, R.layout.spinner_layout);
                            break;
                        case "Chandigarh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_chandigarh_districts, R.layout.spinner_layout);
                            break;
                        case "Dadra and Nagar Haveli": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_dadra_nagar_haveli_districts, R.layout.spinner_layout);
                            break;
                        case "Daman and Diu": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_daman_diu_districts, R.layout.spinner_layout);
                            break;
                        case "Delhi": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_delhi_districts, R.layout.spinner_layout);
                            break;
                        case "Jammu and Kashmir": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_jammu_kashmir_districts, R.layout.spinner_layout);
                            break;
                        case "Lakshadweep": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_lakshadweep_districts, R.layout.spinner_layout);
                            break;
                        case "Ladakh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_ladakh_districts, R.layout.spinner_layout);
                            break;
                        case "Puducherry": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_puducherry_districts, R.layout.spinner_layout);
                            break;
                        default:  break;
                    }
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    districtSpinner.setAdapter(districtAdapter);        //Populate the list of Districts in respect of the State selected

                    //To obtain the selected District from the spinner
                    districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedDistrict = districtSpinner.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvStateSpinner = findViewById(R.id.TextView_indian_states);
        tvDistrictSpinner = findViewById(R.id.TextView_indian_districts);*/

//        submitButton.setOnClickListener(v -> {
//            if (selectedState.equals("Select Your State")) {
//                Toast.makeText(Register.this, "Please select your state from the list", Toast.LENGTH_LONG).show();
//                tvStateSpinner.setError("State is required!");      //To set error on TextView android:fontFamily="@font/times_new_roman"
//                tvStateSpinner.requestFocus();
//            } else if (selectedDistrict.equals("Select Your District")) {
//                Toast.makeText(Register.this, "Please select your district from the list", Toast.LENGTH_LONG).show();
//                tvDistrictSpinner.setError("District is required!");
//                tvDistrictSpinner.requestFocus();
//                tvStateSpinner.setError(null);                      //To reove error from stateSpinner
//            } else {
//                tvStateSpinner.setError(null);
//                tvDistrictSpinner.setError(null);
//                Toast.makeText(Register.this, "Selected State: "+selectedState+"\nSelected District: "+selectedDistrict, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void ViewDetailsAPI() {
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.viewdetails,null);
            ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow.showAtLocation(main_rl, Gravity.CENTER, 0, 0);
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
                    sp_state.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
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

                    sp_city.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, CityName));
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
        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

/*
    public  void  RegisterA(String name,String lanme,String mob,String email,String cast,String state,String distric,String image){
        String otp1 = new GlobalAppApis().RegisterA(name,lanme,mob,email,cast,state,distric,image);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.RegistrationAPI(otp1);
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
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Register.this, call1, "", true);
    }
*/


    public void RegisterA(String name,String lanme,String mob,String email,String cast,String state,String distric,String image) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"Registration", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test1212",jsonString);

                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                        if (status.equalsIgnoreCase("true")) {
                            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = layoutInflater.inflate(R.layout.viewdetails_reg, null);
                            ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
                            TextView msg_tv=customView.findViewById(R.id.msg_tv);
                            msg_tv.setText(object.getString("Msg"));
                            popupWindow2 = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            //display the popup window
                            popupWindow2.showAtLocation(main_rl, Gravity.CENTER, 0, 0);
                            popupWindow2.setFocusable(true);
                            // Settings disappear when you click somewhere else
                            popupWindow2.setOutsideTouchable(true);
                            popupWindow2.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                            popupWindow2.update();

                            Goback.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
//                                    BottomSheetDialog bottomSheet = new BottomSheetDialog();
//                                    bottomSheet.show(getSupportFragmentManager(), "Full days");
                                }
                            });

                        /*    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            //then we will inflate the custom alert dialog xml that we created
                            View dialogView = LayoutInflater.from(Register.this).inflate(R.layout.register_popup, viewGroup, false);
                            TextView img = (TextView) dialogView.findViewById(R.id.img);
                            img.setText();

                            //Now we need an AlertDialog.Builder object
                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            //setting the view of the builder to our custom view that we already inflated
                            builder.setView(dialogView);
                            //finally creating the alert dialog and displaying it
                            alertDialog = builder.create();
                            alertDialog.show();*/
//
//                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
//                            login_editor = login_preference.edit();
//                            login_editor.putString("userid",object.getString("UserId"));
//                            login_editor.commit();

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
                Toast.makeText(Register.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("FirstName",name);
                params.put("LastName", lanme);
                params.put("EmailID", "");
                params.put("MobileNo", mob);
                params.put("Caste", "सवर्ण");
                params.put("StateId", "");
                params.put("DistrictId", "");
                params.put("ProfilePic", "");
                params.put("Password", pasword_et.getText().toString());

//                params.put("FirstName",name);
//                params.put("LastName", lanme);
//                params.put("EmailID", email);
//                params.put("MobileNo", mob);
//                params.put("Caste", "सवर्ण");
//                params.put("StateId", state);
//                params.put("DistrictId", distric);
//                params.put("ProfilePic", temp);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
        requestQueue.add(stringRequest);
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Register.this);
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
        profile_img.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }



}