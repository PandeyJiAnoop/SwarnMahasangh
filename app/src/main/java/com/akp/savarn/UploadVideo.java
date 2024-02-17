package com.akp.savarn;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.akp.savarn.Basic.Api_Urls;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UploadVideo extends AppCompatActivity {
    ImageView back_btn;
    private VideoView video;
    private static final String VIDEO_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
LinearLayout  select_image_poster_ll;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 3;
    private String userChoosenTask;
    String temp;
    ImageView poster_img;

    TextView upload_video_tv,follow_count_txt;
    EditText des_tv,time_duration_tv,video_url_et;
    private SharedPreferences sharedPreferences;
    String userid;


    Spinner sp_state;
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    String stateid,selectedVideoPath;

    RelativeLayout ll;
    private String uriString;
    private byte[] bytes;
    private String Document,usernam;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_upload_video);
        select_image_poster_ll=findViewById(R.id.select_image_poster_ll);
        poster_img=findViewById(R.id.poster_img);
        upload_video_tv=findViewById(R.id.upload_video_tv);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");


        follow_count_txt=findViewById(R.id.follow_count_txt);
        follow_count_txt.setText("@_"+userid);
        sp_state=findViewById(R.id.sp_state);
        des_tv=findViewById(R.id.des_tv);
        time_duration_tv=findViewById(R.id.time_duration_tv);
        video_url_et=findViewById(R.id.video_url_et);


        video=findViewById(R.id.video);
        back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll=findViewById(R.id.ll);
        getCategory();



        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        upload_video_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (des_tv.getText().toString().equalsIgnoreCase("")){
                    des_tv.setError("Add description about video(max limit 150 words)");
                    des_tv.requestFocus();
                }
                else {
                    UploadVedioAPI();
                }
            }
        });


        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
        select_image_poster_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

//        showPictureDialog();

     /*   video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });*/

       /* ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });*/

    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select video from gallery",
                "Record video from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseVideoFromGallary();
                                break;
                            case 1:
                                takeVideoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void chooseVideoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takeVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("result",""+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what","cancle");
            return;
        }
        if (requestCode == GALLERY) {
            Log.d("what","gale");
            if (data != null) {
                Uri contentURI = data.getData();
                selectedVideoPath = getPath(contentURI);
                saveVideoToInternalStorage(selectedVideoPath);
                video.setVideoURI(contentURI);
                video.requestFocus();
                video.start();
                Log.d("pathwrwrwr",""+contentURI);
                ConvertToString(contentURI);
//                Tecket_Attach.setText(contentURI.getLastPathSegment());
//                Log.d("path12345",contentURI.getLastPathSegment());

            }
        }
        else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            selectedVideoPath = getPath(contentURI);
            Log.d("path",selectedVideoPath);
            saveVideoToInternalStorage(selectedVideoPath);
            video.setVideoURI(contentURI);
            video.requestFocus();
            video.start();
            ConvertToString(contentURI);

        }
        else if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }


    }

    private void saveVideoToInternalStorage (String filePath) {
        File newfile;
        try {
            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }
            if(currentFile.exists()){
                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);
                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            }else{
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }}
    private void selectImage() {
        final CharSequence[] items = {"Choose from Library","Cancel" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UploadVideo.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(UploadVideo.this);
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
        poster_img.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }

    public void UploadVedioAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"CreatePost", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String msg = jsonObject.getString("Status");
                    Toast.makeText(getApplicationContext(),jsonObject.getString("Msg"),Toast.LENGTH_LONG).show();
                    if (msg.equalsIgnoreCase("true")){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Msg"),Toast.LENGTH_LONG).show();
                        finish();
                    }
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
                Toast.makeText(UploadVideo.this, "Please Fill All Details!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Category","");
                params.put("Image",temp);
                params.put("Location","");
                params.put("Dis",des_tv.getText().toString());
                params.put("UserId",userid);
                params.put("VideoUrl",video_url_et.getText().toString());
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(UploadVideo.this);
        requestQueue.add(stringRequest);
    }

    void getCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetPostCategory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
//                    StateName.add("Select Category");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId.add(jsonObject1.getString("CategoryID"));
                        String statename = jsonObject1.getString("CategoryName");
                        StateName.add(statename);
                    }

                    sp_state.setAdapter(new ArrayAdapter<String>(UploadVideo.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadVideo.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Action", "7");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(UploadVideo.this);
        requestQueue.add(stringRequest);
    }














    public void ConvertToString(Uri uri){
        uriString = uri.toString();
        Log.d("data", "onActivityResult: uri"+uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            bytes=getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes,Base64.DEFAULT);

            Document=Base64.encodeToString(bytes,Base64.NO_WRAP);
            Log.d("sresponse", "onActivityResult: " + Document);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }



    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}