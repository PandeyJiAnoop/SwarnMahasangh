package com.akp.savarn.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.akp.savarn.DashboardActivity;
import com.akp.savarn.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullImagePage extends AppCompatActivity {
    //Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private Matrix matrix = new Matrix();
    ViewPager ivProductPic;
    public static String Path="";
    ImageView ivcross;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_page);
        ivcross=findViewById(R.id.imageView40);
        Path=getIntent().getStringExtra("Path");
        Picasso.get().load(Path).into(ivcross);
        Log.v("dadada",Path);

        ivcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoViewAttacher photoAttacher;
                photoAttacher= new PhotoViewAttacher(ivcross);
                photoAttacher.update();
            } });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
    }
}
