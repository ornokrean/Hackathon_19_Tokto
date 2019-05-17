package io.agora.openacall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
                break;
            case R.id.menuButtonPodcast:
                text = "podcast";
                break;
            case R.id.menuButtonTalk:
                text = "talk";
                break;
        }
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}
