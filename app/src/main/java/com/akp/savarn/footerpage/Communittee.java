package com.akp.savarn.footerpage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.akp.savarn.DashboardActivity;
import com.akp.savarn.R;
import com.akp.savarn.UploadVideo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Communittee extends AppCompatActivity {
    AppCompatButton follow_btn, message_btn;
    int i = 0;
    LinearLayout all_cmember_ll, all_member_ll,ll,ll1;
    ImageView back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_communittee);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        follow_btn = findViewById(R.id.follow_btn);
        message_btn = findViewById(R.id.message_btn);
        ll = findViewById(R.id.ll);
        ll1 = findViewById(R.id.ll1);

        all_member_ll = findViewById(R.id.all_member_ll);
        all_cmember_ll = findViewById(R.id.all_cmember_ll);



        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow_btn.setBackgroundResource(R.drawable.bg_btn);
                message_btn.setBackgroundResource(R.drawable.rectoval);

                all_cmember_ll.setVisibility(View.GONE);
                all_member_ll.setVisibility(View.VISIBLE);

                /*if (i == 0) {
                    follow_btn.setText("Following");
                    follow_btn.setBackgroundResource(R.drawable.bg_search);
                    i++;
                } else if (i == 1) {
                    follow_btn.setText("Follow");
                    follow_btn.setBackgroundResource(R.drawable.bg_btn);
                    i = 0;
                }*/
            }
        });
        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow_btn.setBackgroundResource(R.drawable.rectoval);
                message_btn.setBackgroundResource(R.drawable.bg_btn);

                all_cmember_ll.setVisibility(View.VISIBLE);
                all_member_ll.setVisibility(View.GONE);

            }
        });

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChatingScreen.class);
                startActivity(intent);
            }
        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChatingScreen.class);
                startActivity(intent);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_member:
                    Intent inta = new Intent(getApplicationContext(), MemberList.class);
                    startActivity(inta);
                    return true;
                case R.id.navigation_add:
                    Intent intent1 = new Intent(getApplicationContext(), UploadVideo.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_comunity:
                    return true;
                case R.id.navigation_profile:
                    Intent inten = new Intent(getApplicationContext(), ProfileScreen.class);
                    inten.putExtra("type","profile");
                    startActivity(inten);
                    return true;
            }
            return false;
        }
    };
}