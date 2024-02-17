package com.akp.savarn.footerpage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akp.savarn.Basic.ChangePassword;
import com.akp.savarn.Basic.SplashScreen;
import com.akp.savarn.Common.FAQ;
import com.akp.savarn.Common.PrivacyPolicy;
import com.akp.savarn.Common.TermsConditions;
import com.akp.savarn.CommunityList;
import com.akp.savarn.DashboardActivity;
import com.akp.savarn.R;
import com.akp.savarn.UploadVideo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingScreen extends AppCompatActivity {
    private RelativeLayout rlFaQ,rlTermsCondition,rlPrivacyPolicy,rlCancelation,donate_ll,about_ll,edit_profile_ll,changePass_rl;
    private TextView tvCallCustomer;
    private ImageView ivMenu;     ImageView fb,telegram,youtube,whatsap,fb1,telegram1,whatsap1,youtube1;
    private AlertDialog alertDialog;
    RelativeLayout community_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_setting_screen);
        rlFaQ=findViewById(R.id.rlFaQ);
        rlTermsCondition=findViewById(R.id.rlTermsCondition);
        rlPrivacyPolicy=findViewById(R.id.rlPrivacyPolicy);
        rlCancelation=findViewById(R.id.rlCancelation);
        tvCallCustomer=findViewById(R.id.tvCallCustomer);
        ivMenu=findViewById(R.id.ivMenu);
        changePass_rl=findViewById(R.id.changePass_rl);
        donate_ll=findViewById(R.id.donate_ll);
        about_ll=findViewById(R.id.about_ll);
        edit_profile_ll=findViewById(R.id.edit_profile_ll);

        community_ll=findViewById(R.id.community_ll);


        fb=findViewById(R.id.fb);
        telegram=findViewById(R.id.telegram);
        youtube=findViewById(R.id.youtube);
        whatsap=findViewById(R.id.whatsap);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        donate_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopupwindowwallet();
            }
        });
        about_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AboutCompanyProfile.class);
                startActivity(intent);
            }
        });



        community_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CommunityList.class);
                startActivity(intent);
            }
        });




        edit_profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getOpenFacebookIntent());
            }
        });

        changePass_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
            }
        });


        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/savarnfoundation/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        whatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/Savarnmfoundat1?t=5oMXG2ZQoGPA-XMCck_dkA&s=08";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        rlFaQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FAQ.class);
                startActivity(intent);
            }
        });
        rlTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), TermsConditions.class);
                startActivity(intent);
                }
        });
        rlPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(intent);
            }
        });
        rlCancelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingScreen.this);
                builder.setMessage(Html.fromHtml("<font color='#FF7F27'>Are you sure want to logout?</font>"));
                builder.setCancelable(false);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                        intent.putExtra("finish", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        startActivity(intent);
                    }
                });
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
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String temp = "tel:" + "9792044448";
                intent.setData(Uri.parse(temp));
                startActivity(intent);
                // hitFilterApi();

            }
        });
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.akp.examcoach", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/savarnabharatf1/"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/savarnabharatf1/"));
        }
    }

    private void showpopupwindowwallet() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.donate_popup_window, viewGroup, false);
        ImageView img = (ImageView) dialogView.findViewById(R.id.img);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent inten = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(inten);
                    return true;
                case R.id.navigation_member:
                    Intent intent = new Intent(getApplicationContext(), MemberList.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_add:
                    Intent intent1 = new Intent(getApplicationContext(), UploadVideo.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_comunity:
                    return true;
                case R.id.navigation_profile:
                    Intent intents = new Intent(getApplicationContext(), ProfileScreen.class);
                    intents.putExtra("type","profile");
                    startActivity(intents);
                    return true;
            }
            return false;
        }
    };


}