package io.agora.openacall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import io.agora.openacall.R;
import io.agora.openacall.model.ConstantApp;

public class MainActivity extends BaseActivity {
    private String serverChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    void makeCall() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(i);
            }
        }, 2000);
    }

    @Override
    protected void initUIandEvent() {
        EditText v_channel = (EditText) findViewById(R.id.channel_name);
        v_channel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = TextUtils.isEmpty(s.toString());
                findViewById(R.id.button_join).setEnabled(!isEmpty);
            }
        });

        String lastChannelName = vSettings().mChannelName;
        if (!TextUtils.isEmpty(lastChannelName)) {
            v_channel.setText(lastChannelName);
            v_channel.setSelection(lastChannelName.length());
        }

//        forwardToRoom();
    }

    @Override
    protected void deInitUIandEvent() {
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onClickJoin(View view) {
        forwardToRoom();
    }

    public void moveToChannel(String channel) {
        vSettings().mChannelName = channel;

        Intent i = new Intent(MainActivity.this, ChatActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);

        startActivity(i);
    }

    public void forwardToRoom() {
//        EditText v_channel = (EditText) findViewById(R.id.channel_name);
//        String channel = v_channel.getText().toString();
        if (true) {
            String channel = "pooping";
            moveToChannel(channel);
        } else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getIntent();
                    String id = Objects.requireNonNull(intent.getExtras()).getString("ID");
                    String content = "{\"id\":\"" + id + "\"}";
                    serverChannel = serverGetMatch(content);
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (serverChannel.equals("0")) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                forwardToRoom();
                            }
                        },
                        5000
                );
            } else {
                moveToChannel(serverChannel);
            }
        }

    }

    private static String baseURL = "http://172.29.110.231:5005/";
    private static String get_match = "get_match";

    public static String serverGetMatch(String content) {
        HttpURLConnection urlConnection;
        URL url;
        try {
            url = new URL(baseURL + get_match);
            urlConnection = (HttpURLConnection) url.openConnection();
            try {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

//                urlConnection.setChunkedStreamingMode(content.length());


                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(content);
                // json data
                writer.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuffer text = new StringBuffer();
                for (String line; (line = br.readLine()) != null; )
                    text.append(line);
                return text.toString();
//                Toast.makeText(getApplicationContext(), loadedText, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
