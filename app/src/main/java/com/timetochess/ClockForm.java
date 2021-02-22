package com.timetochess;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import java.time.Clock;
import java.util.Dictionary;
import java.util.Hashtable;

public class ClockForm extends AppCompatActivity {

    NumberPicker hourTimePicker, minTimePicker, secondsTimePicker;
    NumberPicker hourBonusPicker, minBonusPicker, secondsBonusPicker;

    int hourTime, minTime, secTime;
    int hourBonus, minBonus, secBonus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_form);

        hourTimePicker = findViewById(R.id.hoursTimePicker);
        minTimePicker = findViewById(R.id.minutesTimePicker);
        secondsTimePicker = findViewById(R.id.secondsTimePicker);
        hourBonusPicker = findViewById(R.id.hoursBonusPicker);
        minBonusPicker = findViewById(R.id.minutesBonusPicker);
        secondsBonusPicker = findViewById(R.id.secondsBonusPicker);

        setMaxAndMin(hourTimePicker, 0, 12);
        setMaxAndMin(minTimePicker, 0, 59);
        setMaxAndMin(secondsTimePicker, 0, 59);
        setMaxAndMin(hourBonusPicker, 0, 12);
        setMaxAndMin(minBonusPicker, 0, 59);
        setMaxAndMin(secondsBonusPicker, 0, 59);
    }

    public void setMaxAndMin(NumberPicker picker, int min, int max){
        picker.setMinValue(min);
        picker.setMaxValue(max);
    }

    public Hashtable getValues(){
        hourTime = hourTimePicker.getValue();
        minTime = minTimePicker.getValue();
        secTime = secondsTimePicker.getValue();
        hourBonus = hourBonusPicker.getValue();
        minBonus = minBonusPicker.getValue();
        secBonus = secondsBonusPicker.getValue();

        long playerTime;
        playerTime = (hourTime*60*60*1000);
        playerTime += (minTime*60*1000);
        playerTime += (secTime*1000);

        long playerBonus;
        playerBonus = (hourBonus*60*60*1000);
        playerBonus += (minBonus*60*1000);
        playerBonus += (secBonus*1000);

        Hashtable clockInfo = new Hashtable();
        clockInfo.put("time", playerTime);
        clockInfo.put("bonus", playerBonus);

        return clockInfo;
    }

    public void setClock(View view){
        Hashtable clockInfo = getValues();

        Intent setClockIntent = new Intent(getApplicationContext(), ChessClock.class);
        setClockIntent.putExtra("millisPerPlayer", (long) clockInfo.get("time"));
        setClockIntent.putExtra("bonusPerPlayer", (long) clockInfo.get("bonus"));
        startActivity(setClockIntent);
    }

    public void setAndSaveClock(View view){


    }
}
