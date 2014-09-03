package com.muecke.tkcompanion.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Swimmer implements Serializable {
    public String name;
    public int lapTime;
    public int round;

    public synchronized void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
        round++;
    }

    public synchronized long getBaseTime() {
        return baseTime;
    }

    private long baseTime;
    public List<Integer> splitTime;

    public Swimmer(String name) {
        this.name=name;
        round = 0;
        lapTime =0;
        baseTime =0;
        splitTime = new ArrayList<Integer>();
    }

    public void setLapTime(int time) {
        lapTime=time;
        splitTime.add(time);
    }
}
