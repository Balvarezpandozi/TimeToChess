package com.timetochess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    Button setClockButton;
    Button setSavedClock;
    Button personalize;
    Button appInformationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setClockButton = findViewById(R.id.setClockButton);
        setSavedClock = findViewById(R.id.savedTimeFormatsButton);
        personalize = findViewById(R.id.personalizeButton);
        appInformationButton = findViewById(R.id.infoButton);
    }

    public void setClock(View view){
        Intent setClockIntent = new Intent(getApplicationContext(), ClockForm.class);
        startActivity(setClockIntent);
    }

    public void openSavedClocks(View view){
        Log.i("SavedClocksButton", "Button pressed.");
    }

    public void openPersonalizeActivity(View view){
        Log.i("PersonalizeButton", "Button Pressed.");
    }

    public void openAppInformation(View view){
        Intent appInfoIntent = new Intent(getApplicationContext(), AppInformation.class);
        startActivity(appInfoIntent);
    }
}
