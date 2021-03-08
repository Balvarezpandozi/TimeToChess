package com.timetochess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Database Version
    public static final int DATABASE_VERSION = 1;

    //Database name
    public static final String DATABASE_NAME = "TimeToChessDatabase";

    //Table names
    public static final String CLOCKS_TABLE = "CLOCKS";

    //Common column names
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_BONUS = "BONUS";

    //TABLE CREATE STATEMENTS

    //Clock create statement
    public static final String CREATE_TABLE_CLOCKS= "CREATE TABLE " + CLOCKS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TIME + " LONG, " + COLUMN_BONUS + " LONG)";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CLOCKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete table if there is one
        db.execSQL("DROP TABLE IF EXISTS " + CLOCKS_TABLE);

        //Create new tables
        onCreate(db);
    }

    //METHODS
        //Add clock configuration
    public boolean addClock(Clock clock){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TIME, clock.getTime());
        cv.put(COLUMN_BONUS, clock.getBonus());

        long insert = db.insert(CLOCKS_TABLE, null, cv);

        db.close();

        return insert != -1;
    }

    //Get all Clocks from database
    public List<Clock> getAllClocks(){
        List<Clock> returnList = new ArrayList<>();
        //get data from database
        String queryString = "SELECT * FROM " + CLOCKS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through the cursor (result set) and create new InboxTask object. put them into the return list.
            do {
                int clockId = cursor.getInt(0);
                long clockTime = cursor.getLong(1);
                long clockBonus = cursor.getLong(2);

                Clock newClock = new Clock(clockId, clockTime, clockBonus);
                returnList.add(newClock);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }

    //Get Clocks by id
    public Clock getClockByID(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + CLOCKS_TABLE + " WHERE ID = " + ID;
        Cursor cursor = db.rawQuery(queryString, null);
        Clock clock = null;

        if (cursor.moveToFirst()){
            int clockId = cursor.getInt(0);
            long clockTime = cursor.getLong(1);
            long clockBonus = cursor.getLong(2);

            clock = new Clock(clockId, clockTime, clockBonus);
        }

        db.close();
        cursor.close();
        return clock;
    }

    //Update Clock by id
    public boolean updateClockByID(Clock clock){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String whereString = COLUMN_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(clock.getId())};

        cv.put(COLUMN_TIME, clock.getTime());
        cv.put(COLUMN_BONUS, clock.getBonus());

        long insert = db.update(CLOCKS_TABLE, cv, whereString, whereArgs);
        db.close();

        return insert != -1;
    }

    //Delete Clock by id
    public boolean deleteClockByID(Clock clock){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CLOCKS_TABLE + " WHERE " + COLUMN_ID + " = " + clock.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            db.close();
            cursor.close();
            return true;
        }
        db.close();
        cursor.close();
        return false;
    }

    //Is Clock table empty
    public boolean isInboxEmpty(){
        //get data from database
        String queryString = "SELECT * FROM " + CLOCKS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            return false;
        }

        cursor.close();
        db.close();
        return true;
    }
}
