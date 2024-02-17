package com.akp.savarn.footerpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.akp.savarn.R;

public class AboutCompanyProfile extends AppCompatActivity {
ImageView menuImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_company_profile);
        menuImg=findViewById(R.id.menuImg);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}