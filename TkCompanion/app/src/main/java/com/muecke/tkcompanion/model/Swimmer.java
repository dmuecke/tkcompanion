package com.muecke.tkcompanion.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Swimmer extends Person implements Serializable {

    public int getTtotal() {
        return ttotal;
    }

    private int ttotal;

    public int getRound() {
        return round;
    }

    private int round;

    public Swimmer(Person person) {
        super(person);
    }
    public int getThreshold() {
        return threshold;
    }


    private int targetTime;
    private int threshold;
    private Swimming.SwimStyle style;
    private Swimming.Distance distance;
    private Swimming.IntervalLength intervalLength;

    public void pushOff(long baseTime) {
        this.startTime = baseTime;
        ttotal = 0;
        round = 1;
    }


    public long getStartTime() {
        return startTime;
    }

    private long startTime;

    public List<Integer> getSplitTime() {
        return splitTime;
    }

    public List<Integer> splitTime = new ArrayList<Integer>();;


    public Swimmer(String name) {
        super();
        setName(name);
        reset();
    }

    public void reset() {
        round = 0;
        startTime =0;
        splitTime.clear();
        targetTime = 0;
        threshold = 0;
        ttotal = 0;
    }

    public void setLapTime(long realtime) {
        int lapTime = (int) (realtime - startTime) / 100;
        if (lapTime > 100) {
            ttotal += lapTime;
            splitTime.add(lapTime);
            startTime = realtime;
            round += 1;

        }
    }

    public void calculateUSRPTTime(Swimming.SwimStyle style, Swimming.Distance distance, int targetTime, Swimming.IntervalLength intervalLength) {
        this.targetTime=targetTime;
        this.style=style;
        this.distance=distance;
        this.intervalLength=intervalLength;
        threshold =targetTime/(distance.getValue()/intervalLength.getValue());
    }

    public int getLapTime() {
        if (splitTime.isEmpty()) {
            return -1;
        } else {
            return splitTime.get(splitTime.size() - 1);
        }
    }

    public int getTotal() {
        if (splitTime.isEmpty()) {
            return -1;
        } else {
            int total = 0;
            for (Integer split : splitTime) {
                total += split;
            }
            return total;
        }
    }

    public static String formatTime(int swimTime) {
        int totmin = swimTime / 600;
        int totsec = swimTime % 600;
        return String.format("%02d:%02d", totmin, totsec/10) + "." + totsec%10;
    }
}
