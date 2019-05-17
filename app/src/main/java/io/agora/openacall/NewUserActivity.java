package io.agora.openacall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class NewUserActivity extends AppCompatActivity {
    boolean male = true;
    String[] topics = new String[]{"art", "clothes", "kids", "movies", "music", "news", "series", "sport", "tech"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        for (String topic : topics){

        }
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

    public void submitNewUser(View view) {
        Intent intent = getIntent();
        String id = Objects.requireNonNull(intent.getExtras()).getString("ID");
        String mail = Objects.requireNonNull(intent.getExtras()).getString("MAIL");
        String name = getPackageName();
        JSONObject profile = new JSONObject();
        JSONObject topics = new JSONObject();

        try {
            profile.put("id", id);
            profile.put("mail", mail);
        } catch (JSONException e) {
            Log.d("JSONException", "id - " + e.getMessage());
        }

        for (int i = 1; i <= 18; i++) {
            ToggleButton TB = (ToggleButton) findViewById(getResources().getIdentifier("topic" + i, "id", name));
            boolean isChecked = TB.isChecked();
            String topic = TB.getTextOn().toString();
            try {
                topics.put(topic, Boolean.toString(isChecked));
            } catch (JSONException e) {
                Log.d("JSONException", "topic:" + topic + " - " + e.getMessage());
            }
        }

        String age = ((Spinner) findViewById(R.id.ageSpinner)).getSelectedItem().toString();

        try {
            profile.put("id", id);
            profile.put("mail", mail);
            profile.put("age", age);
        } catch (JSONException e) {
            Log.d("JSONException", "id - " + e.getMessage());
        }
    }
}
