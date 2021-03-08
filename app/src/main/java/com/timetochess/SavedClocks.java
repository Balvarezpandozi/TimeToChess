package com.timetochess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SavedClocks extends AppCompatActivity {

    //Declare variables
    ListView clocksListView;
    ArrayAdapter clocksAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_clocks);

        //Initialize variables
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        clocksListView = findViewById(R.id.clocksListView);
        clocksAdapter = new ArrayAdapter<>(SavedClocks.this, android.R.layout.simple_list_item_1, dataBaseHelper.getAllClocks());
        clocksListView.setAdapter(clocksAdapter);

        //Sets up items' menu
        registerForContextMenu(clocksListView);

        //Sets click on items to find clock in Database, Open clock activity, and send the Clock's time values
        clocksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent setClockIntent = new Intent(getApplicationContext(), ChessClock.class);
                Clock clock = (Clock) parent.getItemAtPosition(position);
                setClockIntent.putExtra("millisPerPlayer", clock.getTime());
                setClockIntent.putExtra("bonusPerPlayer", clock.getBonus());
                startActivity(setClockIntent);
            }
        });
    }

    //Items Menu Set Up
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savedclockmenu, menu);
    }

    //Defines action when clicking option in items' menu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Clock clock = (Clock) clocksListView.getItemAtPosition(position);

        //Plan to further expansion of options, such as, edit the clock
        switch (item.getItemId()){
//            case R.id.edit:
//                editClock(clock);
//                return true;
            case R.id.delete:
                deleteClock(clock);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

//    public void editClock(Clock item){
//        Log.i("menu", "edit clock");
//    }

    //Deletes clock from Database, and updates listview
    public void deleteClock(Clock item){
        dataBaseHelper.deleteClockByID(item);
        clocksAdapter = new ArrayAdapter<>(SavedClocks.this, android.R.layout.simple_list_item_1, dataBaseHelper.getAllClocks());
        clocksListView.setAdapter(clocksAdapter);
        clocksAdapter.notifyDataSetChanged();
    }
}
