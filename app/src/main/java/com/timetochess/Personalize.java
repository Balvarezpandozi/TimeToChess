package com.timetochess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Personalize extends AppCompatActivity {

    //Declare Variables
    EditText nameP1;
    EditText nameP2;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize);

        //Initialize variables
        sharedPreferences = this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);

        nameP1 = findViewById(R.id.player1NameEditText);
        nameP2 = findViewById(R.id.player2NameEditText);

        //Reference
        //  Delete names from shared preferences
        //      sharedPreferences.edit().remove("player1").remove("player2").apply();
        //  Add names to shared preferences
        //      sharedPreferences.edit().putString("player1", "Bryan").putString("player2", "Melissa").apply();
    }

    public void applyChanges(View view){
        //if something written, then
        if (nameP1.getText().length()>0 || nameP2.getText().length()>0){
            //make the text the names for the players
            sharedPreferences.edit()
                    .putString("player1", String.valueOf(nameP1.getText()))
                    .putString("player2", String.valueOf(nameP2.getText()))
                    .apply();
            //and start the clock to show changes
            Intent chessClockIntent = new Intent(getApplicationContext(), ChessClock.class);
            startActivity(chessClockIntent);
        }else{
            //Otherwise, tell the user that they did not input text.
            Toast.makeText(this, getResources().getString(R.string.add_name), Toast.LENGTH_SHORT).show();
        }

    }
}