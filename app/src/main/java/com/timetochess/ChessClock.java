package com.timetochess;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChessClock extends AppCompatActivity {

    Button settingsButton;
    Button pauseStartButton;
    Button resetButton;

    TextView clockPlayer1;
    TextView clockPlayer2;

    int activePlayer;
    boolean isClockActive;

    CountDownTimer timerP1;
    long timeForP1;
    long millisLeftP1;

    CountDownTimer timerP2;
    long timeForP2;
    long millisLeftP2;

    long timePerPlayer;
    long bonusPerPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_clock);

        settingsButton = findViewById(R.id.settingsButton);
        pauseStartButton = findViewById(R.id.pauseAndStartButton);
        resetButton = findViewById(R.id.resetButton);

        activePlayer = 0;
        isClockActive = false;

        Intent intent = getIntent();
        timePerPlayer = intent.getLongExtra("millisPerPlayer", -1);
        bonusPerPlayer = intent.getLongExtra("bonusPerPlayer", 0);

        if (timePerPlayer !=-1){
            timeForP1 = timePerPlayer;
            timeForP2 = timePerPlayer;
        }else{
            timeForP1 = 300000;
            timeForP2 = 300000;
        }

        millisLeftP1 = timeForP1;
        millisLeftP2 = timeForP2;

        clockPlayer1 = findViewById(R.id.player1ClockTextView);
        clockPlayer2 = findViewById(R.id.player2ClockTextView);

        setClock();
    }

    public void resetClock(View view){
        timerP1.cancel();
        timerP2.cancel();
        millisLeftP1 = timeForP1;
        millisLeftP2 = timeForP2;
        clockPlayer1.setText(formatClockNumber(millisLeftP1/(1000*60)) + ":" + formatClockNumber((millisLeftP1/1000)-(millisLeftP1/(1000*60))*60));
        clockPlayer2.setText(formatClockNumber(millisLeftP2/(1000*60)) + ":" + formatClockNumber((millisLeftP2/1000)-(millisLeftP2/(1000*60))*60));
    }

    public void setClock(){
        clockPlayer1.setText(formatClockNumber(timeForP1/(1000*60)) + ":" + formatClockNumber((timeForP1/1000)-(timeForP1/(1000*60))*60));
        clockPlayer2.setText(formatClockNumber(timeForP2/(1000*60)) + ":" + formatClockNumber((timeForP2/1000)-(timeForP2/(1000*60))*60));
    }

    public void openSettings(View view){
        Intent settingsIntent = new Intent(getApplicationContext(), Settings.class);
        startActivity(settingsIntent);
    }

    public void resumeClock(){
        timerP1 = new CountDownTimer(millisLeftP1, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeftP1=millisUntilFinished;
                long min = (millisLeftP1/(1000*60));
                long sec = ((millisLeftP1/1000)-min*60);
                clockPlayer1.setText(formatClockNumber(min) + ":" + formatClockNumber(sec));
                clockPlayer2.setText(formatClockNumber(millisLeftP2/(1000*60)) + ":" + formatClockNumber((millisLeftP2/1000)-(millisLeftP2/(1000*60))*60));
            }

            @Override
            public void onFinish() {
                clockPlayer1.setText("Time Out!");
            }
        };

        timerP2 = new CountDownTimer(millisLeftP2, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeftP2=millisUntilFinished;
                long min = (millisLeftP2/(1000*60));
                long sec = ((millisLeftP2/1000)-min*60);
                clockPlayer2.setText(formatClockNumber(min) + ":" + formatClockNumber(sec));
                clockPlayer1.setText(formatClockNumber(millisLeftP1/(1000*60)) + ":" + formatClockNumber((millisLeftP1/1000)-(millisLeftP1/(1000*60))*60));
            }

            @Override
            public void onFinish() {
                clockPlayer2.setText("Time Out!");
            }
        };
    }

    public void pauseStartClock(View view){
        Log.i("VARIABLES", "\n\nisClockActive= " + isClockActive + "\nactivePlayer= " + activePlayer + "\n");
        if(isClockActive){
            timerP1.cancel();
            timerP2.cancel();
            pauseStartButton.setText(R.string.start);
            isClockActive = false;
            settingsButton.setEnabled(true);
            resetButton.setEnabled(true);
        } else {
            pauseStartButton.setText(R.string.pause);
            settingsButton.setEnabled(false);
            resetButton.setEnabled(false);
            if (activePlayer==0){
                resumeClock();
                timerP1.start();
            } else {
                resumeClock();
                timerP2.start();
            }
            settingsButton.setEnabled(false);
            resetButton.setEnabled(false);
            isClockActive = true;
        }


        Log.i("pauseStartButton", "Button pressed.");
    }

    public void switchToP1(View view){
        if (isClockActive){
            addBonusTime(bonusPerPlayer, activePlayer);
            activePlayer = 0;
            clockPlayer1.setEnabled(true);
            clockPlayer2.setEnabled(false);
            timerP2.cancel();
            resumeClock();
            timerP1.start();
        }
    }

    public void switchToP2(View view){
        if (isClockActive){
            addBonusTime(bonusPerPlayer, activePlayer);
            activePlayer = 1;
            clockPlayer1.setEnabled(false);
            clockPlayer2.setEnabled(true);
            timerP1.cancel();
            resumeClock();
            timerP2.start();
        }
    }

    public String formatClockNumber(long num){
        String formattedNum;
        if (num <= 9) {
            formattedNum = "0" + num;
        } else {
            return String.valueOf(num);
        }
        return formattedNum;
    }

    public void addBonusTime(long bonus, int player){
        if (player==0){
            millisLeftP1 += bonus;
        }else{
            millisLeftP2 += bonus;
        }
    }
}
