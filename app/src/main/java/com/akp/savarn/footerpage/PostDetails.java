package com.akp.savarn.footerpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.akp.savarn.R;
import com.akp.savarn.UploadVideo;

public class PostDetails extends AppCompatActivity {
ImageView more_img;
    PopupMenu popupmenu ;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_post_details);
        back_btn=findViewById(R.id.back_btn);

        more_img=findViewById(R.id.more_img);
        more_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopMenuDisplay();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void PopMenuDisplay() {
        popupmenu = new PopupMenu(PostDetails.this, more_img);
        popupmenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupmenu.getMenu());
        popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(PostDetails.this, item.getTitle(),Toast.LENGTH_LONG).show();
                if (item.getTitle().equals("Edit")) {
                    Intent intent=new Intent(getApplicationContext(), UploadVideo.class);
                    startActivity(intent);

                }
               else if (item.getTitle().equals("Delete")) {
                    Toast.makeText(PostDetails.this, "Move to Draft Successfully",Toast.LENGTH_LONG).show();

                }else if (item.getTitle().equals("Move to Draft")) {
                    Toast.makeText(PostDetails.this, "Move to Draft Successfully",Toast.LENGTH_LONG).show();

                }

                return true;
            }
        });

        popupmenu.show();

    }
}

