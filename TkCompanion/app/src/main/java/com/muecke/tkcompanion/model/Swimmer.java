package com.muecke.tkcompanion.model;

import android.os.SystemClock;
import android.util.Log;

import com.dorjeduck.xyz.AccurateCountDownTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Swimmer extends Person implements Serializable {

    private int lastLap = 0;
    private AccurateCountDownTimer cdt;
    private int lanesPerInterval = 1;

    public int getTtotal() {
        return ttotal;
    }

    public void addLastLapToTotal() {
        ttotal += lastLap;
    }
    private int ttotal;

    public int getRound() {
        return round;
    }

    public void incRound() {
        round += 1;
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
        lastLap = 0;
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
        lastLap = 0;
        lanesPerInterval = 1;
    }

    public void setLapTime(long realtime) {
        int lapTime = (int) (realtime - startTime) / 100;
        if (lapTime > 100) {
            splitTime.add(lapTime);
            startTime = realtime;
            lastLap = lapTime;

        }
    }

    public int getLapTime() {
        return lastLap;
    }

    public int getTotal() {
        if (splitTime.isEmpty()) {
            return 0;
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
        return String.format("%02d:%02d.%d", totmin, totsec/10, totsec%10);
    }

    /*
        Push-off interval training
     */
    public void pushOff(int interval, long realtime) {
        ttotal = 0;
        runCountDown(interval, realtime);
    }

    private void runCountDown(final int interval, final long realtime) {
        startTime = realtime;

        round += 1;
        lastLap = 0;
        Log.d("runCountDown", getName());
        cdt = new AccurateCountDownTimer(interval * 1000, 3 * 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long elapsed = elapsedRealtime - realtime;
                ttotal += elapsed / 100;

                runCountDown(interval, elapsedRealtime);
            }
        };
        cdt.start();
//        Log.d("timer", "started for " + getName());
    }

    public void stopInterval() {
        if (cdt != null) {
            Log.d("stopInterval", getName());

            cdt.cancel();
            cdt = null;
        }
    }


    public Result getResult(int lanesPerInterval) {
        Result r = new Result(getName(), 0, "", Competition.getShortDesc(), getTtotal(), Competition.getPoolSize(), lanesPerInterval);
        r.splitTime.addAll(splitTime);

        return r;
    }

    public void setLanesPerInterval(int lanesPerInterval) {
        this.lanesPerInterval = lanesPerInterval;
    }
}
