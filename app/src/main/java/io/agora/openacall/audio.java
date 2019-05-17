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
    MediaPlayer po1;
    MediaPlayer po2;
    MediaPlayer po3;
    MediaPlayer po4;
    MediaPlayer po5;
    MediaPlayer po6;
    MediaPlayer po7;
    MediaPlayer temp;
    ConstraintLayout swipeLayout;
    boolean bar;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        swipeLayout =(ConstraintLayout) findViewById(R.id.audio_layout);
        mp = MediaPlayer.create(audio.this, R.raw.never_gonna);
        po = MediaPlayer.create(audio.this, R.raw.podcast);
        Bundle extras = getIntent().getExtras();
        boolean t = extras.getBoolean("bool");
        bar=t;
        if(t){
            mp.start();
            swipeLayout.setBackgroundResource(R.drawable.music_back);
            po1 =MediaPlayer.create(audio.this,R.raw.c1);
            po2 =MediaPlayer.create(audio.this,R.raw.c2);
            po3 =MediaPlayer.create(audio.this,R.raw.c3);
            po1 =MediaPlayer.create(audio.this,R.raw.c4);
            po1 =MediaPlayer.create(audio.this,R.raw.c5);
            po1 =MediaPlayer.create(audio.this,R.raw.c6);
            po1 =MediaPlayer.create(audio.this,R.raw.c7);
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
                index = (index+1)%8;
                if(bar){
                    switch (index){
                        case 0:
                            if(po7.isPlaying()){
                                po7.stop();
                            }
                            mp.start();
                            temp= mp;
                        break;
                        case 1:
                            mp.stop();
                            po1.start();
                            temp=po1;
                        break;
                        case 2:
                            po1.stop();
                            po2.start();
                            temp = po2;
                        break;
                        case 3:
                            po2.stop();
                            po3.start();
                            temp= po3;
                            break;
                        case 4:
                            po3.stop();
                            po4.start();
                            temp = po4;
                            break;
                        case 5:
                            po4.stop();
                            po5.start();
                            temp = po5;
                            break;
                        case 6:
                            po5.stop();
                            po6.start();
                            temp =po6;
                            break;
                        case 7:
                            po6.stop();
                            po7.start();
                            temp = po7;
                    }
                }
            }
        });
    }

    @Override
    public void onStop () {
//do your stuff here
        if(temp.isPlaying()){
            temp.stop();
        }
        if(mp.isPlaying()){
            mp.stop();
        }
        if(po.isPlaying()){
            po.stop();
        }
        super.onStop();
    }
}
