package com.timetochess;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public class Clock {
    private int id;
    private long time;
    private long bonus;

    //Constructor
    Clock(int id, long time, long bonus){
        this.id = id;
        this.time = time;
        this.bonus = bonus;
    }

    @NonNull
    @Override
    public String toString() {
        long minTime = (time/(1000*60));
        long secTime = (time/(1000))-((time/(1000*60))*60);
        long minBonus = (bonus/(1000*60));
        long secBonus = (bonus/(1000))-((bonus/(1000*60))*60);
        return "Time: " + minTime + ":" + secTime + " - Bonus: " + minBonus + ":" + secBonus;
    }

    //Getters ans Setters
    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time = time;
    }

    public long getBonus() {
        return bonus;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }
}
