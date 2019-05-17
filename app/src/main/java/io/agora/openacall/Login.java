package io.agora.openacall;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.agora.openacall.ui.MainActivity;

public class Login extends AppCompatActivity {
    static String TAG = "LOGIN";
    private FirebaseAuth mAuth;
    private static String baseURL = "http://172.29.110.231:5005/";
    private static String getID = "get_id";
    private boolean isFlaskNewUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean createFlaskUser(FirebaseUser user) {
        HttpURLConnection urlConnection;
        URL url;
        String content = "{" + user.getUid() + ":{\"mail\":\"" + user.getEmail() + "\"}}";
        try {
            url = new URL(baseURL + getID);
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
                isFlaskNewUser = text.equals("1");
                return isFlaskNewUser;
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
        return false;
    }

    public void updateUI(FirebaseUser user, boolean newUser) {
        final FirebaseUser u = user;
        if (user != null) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    createFlaskUser(u);
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            newUser = newUser || isFlaskNewUser;

            if (newUser) {
                Intent intent = new Intent(getBaseContext(), NewUserActivity.class);
                intent.putExtra("USER", user.getUid());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                intent.putExtra("USER", user.getUid());
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Could not log in", Toast.LENGTH_LONG).show();
        }
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user, false);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null, true);
                }
            }
        });
    }

    public void onLoginClickEvent(View view) {
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, true);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            login(email, password);
                        }

                        // ...
                    }
                });


    }
}
