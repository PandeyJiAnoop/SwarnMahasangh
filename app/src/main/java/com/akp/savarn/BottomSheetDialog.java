package com.akp.savarn;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.akp.savarn.Basic.Api_Urls;
import com.akp.savarn.footerpage.ScreenshotUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private SharedPreferences sharedPreferences;
    String Userid;
    ImageView profile_img;
    TextView name_tv,car_no_tv,loca_tv,membersince_tv;

    LinearLayout parentView;

    Bitmap bitmap;
    String ImagePath;
    Uri URI;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.card_generate,
                container, false);
        sharedPreferences = getContext().getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("userid", "");
        ImageButton share_ib = v.findViewById(R.id.share_ib);
        ImageButton copy_ib = v.findViewById(R.id.copy_ib);
        ImageButton download_ib = v.findViewById(R.id.download_ib);
        ImageButton sharelink_ib = v.findViewById(R.id.sharelink_ib);
        ImageView cancel_img = v.findViewById(R.id.cancel_img);

        parentView= v.findViewById(R.id.parentView);
        download_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = ScreenshotUtil.getInstance().takeScreenshotForView(parentView); // Take ScreenshotUtil for any view
                requestPermissionAndSave();
            }
        });

        name_tv = v.findViewById(R.id.name_tv);
        car_no_tv = v.findViewById(R.id.car_no_tv);
        loca_tv = v.findViewById(R.id.loca_tv);
        membersince_tv = v.findViewById(R.id.membersince_tv);
        profile_img = v.findViewById(R.id.profile_img);

        RelativeLayout rl= v.findViewById(R.id.rl);

        getcardDetails();

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getActivity(),DashboardActivity.class);
                startActivity(intent);
                dismiss();
            } });

        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DashboardActivity.class);
                startActivity(intent);
                dismiss();
            } });

        share_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DashboardActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "Whatsap Share", Toast.LENGTH_SHORT).show();
                dismiss();
            } });

        copy_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   Intent intent=new Intent(getActivity(),DashboardActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "Copy Link", Toast.LENGTH_SHORT).show();
                dismiss();
            } });

//        download_ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            { Intent intent=new Intent(getActivity(),DashboardActivity.class);
//                startActivity(intent);
//                Toast.makeText(getActivity(),
//                        "Download", Toast.LENGTH_SHORT)
//                        .show();
//                dismiss();
//            }
//        });

        sharelink_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                takeScreenShot(parentView);
            }
        });

        return v;
    }



    public void getcardDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                                Glide.with(getContext()).load(jsonobject.getString("ProfilePhoto")).error(R.drawable.noimage).into(profile_img);
                            }


                        }
                    }
                    else {
                        Toast.makeText(getContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", Userid);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void requestPermissionAndSave() {

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (bitmap != null) {
                            ImagePath = MediaStore.Images.Media.insertImage(
                                    getActivity().getContentResolver(),
                                    bitmap,
                                    "demo_image",
                                    "demo_image"
                            );

                            URI = Uri.parse(ImagePath);

                            Toast.makeText(getContext(), "Image Saved Successfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getActivity(),DashboardActivity.class);
                            startActivity(intent);
//                            String path = Environment.getExternalStorageDirectory().toString() + "/test.png";
//                            FileUtil.getInstance().storeBitmap(bitmap, path);
//                            Toast.makeText(activity, getString(R.string.toast_message_screenshot_success) + " " + path, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.toast_message_screenshot), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Toast.makeText(getContext(), getString(R.string.settings_message), Toast.LENGTH_LONG).show();
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
                    getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
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

        Uri uri =  FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Download सवर्ण जोड़ो अभियान From PlayStore \n https://play.google.com/store/apps/details?id=com.akp.savarn");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No App Available", Toast.LENGTH_SHORT).show();
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

