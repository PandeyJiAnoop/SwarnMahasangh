package com.akp.savarn.Basic;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akp.savarn.BottomSheetDialog;
import com.akp.savarn.DashboardActivity;
import com.akp.savarn.R;

public class SangatanSelect extends AppCompatActivity {
    TextView  explore_tv;
    ImageView menuImg;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sangatan_select);
        explore_tv=findViewById(R.id.explore_tv);
        ll=findViewById(R.id.ll);
        menuImg=findViewById(R.id.menuImg);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(),
                        "Full days");
            }
        });
      explore_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

    }
}