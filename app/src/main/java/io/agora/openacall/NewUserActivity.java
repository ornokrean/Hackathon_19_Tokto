package io.agora.openacall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ToggleButton;
import io.agora.openacall.Login;

import com.google.android.gms.common.util.ArrayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class NewUserActivity extends AppCompatActivity {
    boolean male = true;
    String[] topics = new String[]{"art", "clothes", "kids", "movies", "music", "news", "series", "sport", "tech"};
    boolean[] markedTopics = new boolean[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
    }

    public void toggleMaleFemale(View view) {
        ImageButton IB = (ImageButton) view;
        int viewID = IB.getId();
        if (viewID == R.id.maleButton) {
            male = true;
            ((ImageButton) findViewById(R.id.maleButton)).setImageResource(R.drawable.male_on);
            ((ImageButton) findViewById(R.id.femaleButton)).setImageResource(R.drawable.female_off);
        } else {
            male = false;
            ((ImageButton) findViewById(R.id.maleButton)).setImageResource(R.drawable.male_off);
            ((ImageButton) findViewById(R.id.femaleButton)).setImageResource(R.drawable.female_on);
        }
    }

    public void toggleTopic(View view) {
        String topic = view.getTag().toString();
        for (int i = 0; i < topics.length; i++) {
            if (topics[i].equals(topic)) {
                markedTopics[i] = !markedTopics[i];
                if (markedTopics[i]) {
                    int resId = getResources().getIdentifier("io.agora.openacall:drawable/" + topic + "_on", null, null);
                    ((ImageButton) view).setImageResource(resId);
                } else {
                    int resId = getResources().getIdentifier("io.agora.openacall:drawable/" + topic + "_off", null, null);
                    ((ImageButton) view).setImageResource(resId);
                }
                break;
            }
        }
    }

    public void submitNewUser(View view) {
        Intent intent = getIntent();
        String id = Objects.requireNonNull(intent.getExtras()).getString("USER");
        String name = getPackageName();
        JSONObject profile = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject obj = new JSONObject();


        for (int i=0;i<topics.length;i++) {
            String buttonName = topics[i];
            boolean isChecked = markedTopics[i];
            ImageButton TB = findViewById(getResources().getIdentifier("topic_" + buttonName, "id", name));
            String topic = TB.getTag().toString();
            try {
                profile.put(topic, isChecked ? 1 : 0);
            } catch (JSONException e) {
                Log.d("JSONException", "topic:" + topic + " - " + e.getMessage());
            }
        }

        String age = ((Spinner) findViewById(R.id.ageSpinner)).getSelectedItem().toString();

        try {
            data.put("profile", profile);
        } catch (JSONException e) {
            Log.d("JSONException", "id - " + e.getMessage());
        }
        try {
            obj.put(id, data);
        } catch (JSONException e) {
            Log.d("JSONException", "id - " + e.getMessage());
        }
        final String postData = obj.toString();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Login.serverCreate(postData);
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent newI = new Intent(getBaseContext(), MenuActivity.class);
        newI.putExtra("ID", id);
        startActivity(newI);
    }
}
