package com.timetochess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    //Declare Variables
    Button setClockButton;
    Button setSavedClock;
    Button personalize;
    Button appInformationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Initialize variables
        setClockButton = findViewById(R.id.setClockButton);
        setSavedClock = findViewById(R.id.savedTimeFormatsButton);
        personalize = findViewById(R.id.personalizeButton);
        appInformationButton = findViewById(R.id.infoButton);
    }

    //Opens form activity to set clock
    public void setClock(View view){
        Intent setClockIntent = new Intent(getApplicationContext(), ClockForm.class);
        startActivity(setClockIntent);
    }

    //Opens saved clocks activity
    public void openSavedClocks(View view){
        Intent savedClocksIntent = new Intent(getApplicationContext(), SavedClocks.class);
        startActivity(savedClocksIntent);
    }

    //Opens personalize activity
    public void openPersonalizeActivity(View view){
        Intent personalizeIntent = new Intent(getApplicationContext(), Personalize.class);
        startActivity(personalizeIntent);
    }

    //Opens the App Information activity
    public void openAppInformation(View view){
        Intent appInfoIntent = new Intent(getApplicationContext(), AppInformation.class);
        startActivity(appInfoIntent);
    }
}
