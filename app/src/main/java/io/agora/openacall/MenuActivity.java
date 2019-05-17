package io.agora.openacall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import io.agora.openacall.ui.MainActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void chooseActivity(View view) {
        ImageButton IB = (ImageButton) view;
        int viewID = IB.getId();
        String text = "";
        switch (viewID) {
            case R.id.menuButtonMusic:
                text = "music";
                Intent i = new Intent(getApplicationContext(),audio.class);
                i.putExtra("bool",true);
                startActivity(i);
                break;
            case R.id.menuButtonPodcast:
                text = "podcast";
                Intent j = new Intent(getApplicationContext(),audio.class);
                j.putExtra("bool",false);
                startActivity(j);
                break;
            case R.id.menuButtonTalk:
                text = "talk";
//                String channel = "pooping";
////                vSettings().mChannelName = channel;
//
//                Intent k = new Intent(MenuActivity.this, ChatActivity.class);
//                k.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);
//
//                startActivity(k);
                Intent k = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(k);
                break;
        }
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}
