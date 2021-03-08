package com.timetochess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.HashMap;

public class ClockForm extends AppCompatActivity {

    //Declare variables
    NumberPicker hourTimePicker, minTimePicker, secondsTimePicker;
    NumberPicker hourBonusPicker, minBonusPicker, secondsBonusPicker;

    int hourTime, minTime, secTime;
    int hourBonus, minBonus, secBonus;

    DataBaseHelper dataBaseHelper;
    Clock clock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_form);

        //Initialize variables. Possible improvement: use array to put variables in and initialize it with loop.
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

        dataBaseHelper = new DataBaseHelper(this);
    }

    //Sets min and max for the number pickers
    public void setMaxAndMin(NumberPicker picker, int min, int max){
        picker.setMinValue(min);
        picker.setMaxValue(max);
    }

    //Creates and returns a Hash Map with the amount of time chosen in milliseconds
    public HashMap getValues(){
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

        HashMap clockInfo = new HashMap();
        clockInfo.put("time", playerTime);
        clockInfo.put("bonus", playerBonus);

        return clockInfo;
    }

    //Opens the clocks activity and sends the time per player and the bonus per player to the Clock activity
    public void setClock(View view){
        HashMap clockInfo = getValues();

        Intent setClockIntent = new Intent(getApplicationContext(), ChessClock.class);
        setClockIntent.putExtra("millisPerPlayer", (long) clockInfo.get("time"));
        setClockIntent.putExtra("bonusPerPlayer", (long) clockInfo.get("bonus"));
        startActivity(setClockIntent);
    }

    //Opens the clock activity and sends time values like the setClock function, but also saves the clock on the database.
    public void setAndSaveClock(View view){
        HashMap clockInfo = getValues();
        String time = String.valueOf(clockInfo.get("time"));
        String bonus = String.valueOf(clockInfo.get("bonus"));

        //Add clock to database
        clock = new Clock(-1, Long.parseLong(time), Long.parseLong(bonus));
        if(dataBaseHelper.addClock(clock)){
            Toast.makeText(getApplicationContext(), "Clock saved succesfully", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Clock couldn't be saved", Toast.LENGTH_LONG).show();
        }
        //Opens clock activity
        Intent setClockIntent = new Intent(getApplicationContext(), ChessClock.class);
        setClockIntent.putExtra("millisPerPlayer", (long) clockInfo.get("time"));
        setClockIntent.putExtra("bonusPerPlayer", (long) clockInfo.get("bonus"));
        startActivity(setClockIntent);
    }
}
