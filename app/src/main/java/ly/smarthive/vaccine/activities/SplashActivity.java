package ly.smarthive.vaccine.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import ly.smarthive.vaccine.R;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);


        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(3000);
                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        background.start();
    }
}