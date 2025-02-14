package com.innovexa.apiintro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable launchHomeActivity = () -> {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish(); // Finish MainActivity to prevent returning back
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.postDelayed(launchHomeActivity, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(launchHomeActivity); // Prevent memory leaks
    }
}
