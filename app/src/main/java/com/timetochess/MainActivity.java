package com.timetochess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View logoImageView = findViewById(R.id.logoImageView);
        final Handler handler = new Handler();

        logoImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoImageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_out));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), ChessClock.class);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
            }
        }, 1000);
    }
}
