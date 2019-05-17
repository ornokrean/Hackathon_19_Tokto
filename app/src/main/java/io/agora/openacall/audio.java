package io.agora.openacall;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import io.agora.openacall.ui.OnSwipeTouchListener;

public class audio extends AppCompatActivity {
    MediaPlayer mp;
    MediaPlayer po;
    ConstraintLayout swipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        swipeLayout =(ConstraintLayout) findViewById(R.id.audio_layout);
        mp = MediaPlayer.create(audio.this, R.raw.never_gonna);
        po = MediaPlayer.create(audio.this, R.raw.podcast);
        Bundle extras = getIntent().getExtras();
        boolean t = extras.getBoolean("bool");
        if(t){
            mp.start();
            swipeLayout.setBackgroundResource(R.drawable.music_back);
        }else {
            po.start();
            swipeLayout.setBackgroundResource(R.drawable.podcast_back);

        }
        swipeLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            @Override
            public void onSwipeUp(){
                Intent mainActivity = new Intent(audio.this, MenuActivity.class);
                startActivity(mainActivity);
            }
            @Override
            public void onDoubleClick(){
            }
        });
    }

    @Override
    public void onStop () {
//do your stuff here
        if(mp.isPlaying()){
            mp.stop();
        }
        if(po.isPlaying()){
            po.stop();
        }
        super.onStop();
    }
}
