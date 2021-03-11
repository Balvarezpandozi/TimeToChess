package com.timetochess;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChessClock extends AppCompatActivity {

    //Buttons: these buttons start, pause, reset the clocks, and take the user to the settings activity
    Button settingsButton;
    Button pauseStartButton;
    Button resetButton;

    //These variables show the player name
    TextView namePlayer1;
    TextView namePlayer2;

    //These variables are the clocks that show the time each player have
    TextView clockPlayer1;
    TextView clockPlayer2;

    //This variable keeps track of who's turn it is
    int activePlayer;

    //This variable indicates if the clocks are counting down
    boolean isClockActive;

    //Clocks: the clocks are two CountDownTimers. There are also variables that hold the initial time and the left time each player has
    //Clock 1 / Player 1
    CountDownTimer timerP1;
    long timeForP1;
    long millisLeftP1;
    //CLock 2 / Player 2
    CountDownTimer timerP2;
    long timeForP2;
    long millisLeftP2;

    //This variable holds the time determined for each player
    //Note: timePerPlayer, timeForP1, and timeForP2 have the same value and purpose. This is because of future expansion. Plan: to set player one and player two time individually
    long timePerPlayer;
    //This variable holds the bonus time determined for each player
    long bonusPerPlayer;

    //These variables help to play sounds when time ends and switching between turns
    MediaPlayer alarmPlayer;
    MediaPlayer switchTurnPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_clock);


        //Variables initialization
        settingsButton = findViewById(R.id.settingsButton);
        pauseStartButton = findViewById(R.id.pauseAndStartButton);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setEnabled(false);

        namePlayer1 = findViewById(R.id.player1NameTextView);
        namePlayer2 = findViewById(R.id.player2NameTextView);

        clockPlayer1 = findViewById(R.id.player1ClockTextView);
        clockPlayer2 = findViewById(R.id.player2ClockTextView);
        clockPlayer2.setEnabled(false);

        activePlayer = 0;
        isClockActive = false;

        alarmPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);

        //Personalization: setting player names
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        namePlayer1.setText(sharedPreferences.getString("player1", getResources().getString(R.string.player_1)));
        namePlayer2.setText(sharedPreferences.getString("player2", getResources().getString(R.string.player_2)));

        //This intent takes the amounts of time determined in settings to initialize variables
        Intent intent = getIntent();
        timePerPlayer = intent.getLongExtra("millisPerPlayer", -1);
        bonusPerPlayer = intent.getLongExtra("bonusPerPlayer", 0);

        //Set time to the determined in settings, OR in case there has not been time assigned, set time to five minutes
        if (timePerPlayer !=-1){
            timeForP1 = timePerPlayer;
            timeForP2 = timePerPlayer;
        }else{
            timeForP1 = 300000;
            timeForP2 = 300000;
        }

        millisLeftP1 = timeForP1;
        millisLeftP2 = timeForP2;

        setClock();
    }

    //Resets time variables, clocks and timers
    public void resetClock(View view){
        timerP1.cancel();
        timerP2.cancel();
        millisLeftP1 = timeForP1;
        millisLeftP2 = timeForP2;
        setTextInClock(clockPlayer1,0);
        setTextInClock(clockPlayer2,1);
        if (activePlayer == 0){
            clockPlayer1.setEnabled(true);
        }else{
            clockPlayer2.setEnabled(true);
        }
    }

    //Shows players' time in the clocks
    public void setClock(){
        setTextInClock(clockPlayer1,0);
        setTextInClock(clockPlayer2,1);
    }

    //Open settings activity
    public void openSettings(View view){
        Intent settingsIntent = new Intent(getApplicationContext(), Settings.class);
        startActivity(settingsIntent);
    }

    //Resumes clock
    public void resumeClock(){
        //Player 1
        timerP1 = new CountDownTimer(millisLeftP1, 1000){
            //Every second updates the clock and the time variables to keep track of the time
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeftP1=millisUntilFinished;
                setTextInClock(clockPlayer1, 0);
            }
            //When finished changes the time to "Time Out!"
            @Override
            public void onFinish() {
                clockPlayer1.setText(R.string.time_out);
                alarmPlayer.start();
                clockPlayer1.setEnabled(false);
                clockPlayer2.setEnabled(false);
            }
        };
        //Player 2
        timerP2 = new CountDownTimer(millisLeftP2, 1000){
            //Every second updates the clock and the time variables to keep track of the time
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeftP2=millisUntilFinished;
                setTextInClock(clockPlayer2, 1);
            }
            //When finished changes the time to "Time Out!"
            @Override
            public void onFinish() {
                clockPlayer2.setText(R.string.time_out);
                alarmPlayer.start();
                clockPlayer1.setEnabled(false);
                clockPlayer2.setEnabled(false);
            }
        };
    }

    //Pauses and Starts clock
    public void pauseStartClock(View view){
        resetButton.setEnabled(true);
        if(isClockActive){
            //If clocks is running
            //Stop timers
            timerP1.cancel();
            timerP2.cancel();
            //Sets buttons
            pauseStartButton.setText(R.string.start);
            settingsButton.setEnabled(true);
            resetButton.setEnabled(true);
            //Indicates that the clock is not running
            isClockActive = false;
        } else {
            //If clock is NOT running
            //Sets buttons
            pauseStartButton.setText(R.string.pause);
            settingsButton.setEnabled(false);
            resetButton.setEnabled(false);
            //Depending on active player: resumes clock
            if (activePlayer==0){
                resumeClock();
                timerP1.start();
            } else {
                resumeClock();
                timerP2.start();
            }
            //Sets clock active variable to true
            isClockActive = true;
        }
    }

    //Switches turn to player 1
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

    //Switches turn to player 2
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

    //Set text in player clock
    @SuppressLint("SetTextI18n")
    public void setTextInClock(TextView clock, int player){
        long min;
        long sec;
        if (player == 0) {
              min = (millisLeftP1/(1000*60));
              sec = ((millisLeftP1/1000)-min*60);
        }else{
            min = (millisLeftP2/(1000*60));
            sec = ((millisLeftP2/1000)-min*60);
        }
        clock.setText(formatClockNumber(min) + ":" + formatClockNumber(sec));
    }

    //If number is less than 10, adds zero before the number. Ex. 09, 05.
    public String formatClockNumber(long num){
        String formattedNum;
        if (num <= 9) {
            formattedNum = "0" + num;
        } else {
            return String.valueOf(num);
        }
        return formattedNum;
    }

    //Adds the bonus time determined to the millisleft depending on what player switched their turn
    public void addBonusTime(long bonus, int player){
        if (player==0){
            millisLeftP1 += bonus;
            setTextInClock(clockPlayer1, player);
        }else{
            millisLeftP2 += bonus;
            setTextInClock(clockPlayer2, player);
        }
    }
}
