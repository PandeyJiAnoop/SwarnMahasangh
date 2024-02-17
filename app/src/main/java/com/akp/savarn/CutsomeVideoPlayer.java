package com.akp.savarn;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class CutsomeVideoPlayer extends YouTubeBaseActivity {

    String getPath,getVideoId;
    String api_key = "AIzaSyCi414BxuwEp_0Nz8OTXOmcW3uy_0Oc59I";

    public static final String API_KEY = "AIzaSyCi414BxuwEp_0Nz8OTXOmcW3uy_0Oc59I";
    public static final String VIDEO_ID = "3gwOblY8_Mc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** attaching layout xml **/
        setContentView(R.layout.activity_cutsome_video_player);

        getPath=getIntent().getStringExtra("Path");

        getVideoId=getPath.replace("https://www.youtube.com/watch?v=","");
        ImageView back_img=findViewById(R.id.back_img);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get reference to the view of Video player
        YouTubePlayerView ytPlayer = (YouTubePlayerView)findViewById(R.id.ytPlayer);

        ytPlayer.initialize(
                api_key,
                new YouTubePlayer.OnInitializedListener() {
                    // Implement two methods by clicking on red
                    // error bulb inside onInitializationSuccess
                    // method add the video link or the playlist
                    // link that you want to play In here we
                    // also handle the play and pause
                    // functionality
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b)
                    {
                        youTubePlayer.loadVideo(getVideoId);
                        youTubePlayer.play();
                    }

                    // Inside onInitializationFailure
                    // implement the failure functionality
                    // Here we will show toast
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult)
                    {
                        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}