package com.example.absentiessect1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);  // Make sure to use your XML layout for Splash Screen

        // Set a delay of 3 seconds (3000 milliseconds) before transitioning to the login screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the delay, move to the Login Activity
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();  // Finish the splash screen activity so that it can't be accessed by pressing the back button
            }
        }, 3000);  // Delay of 3 seconds
    }
}
