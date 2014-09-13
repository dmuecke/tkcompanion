package com.muecke.tkcompanion.model;

import com.muecke.tkcompanion.Evaluable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Swimmer implements Evaluable, Serializable {
    public String name;
    public int lapTime;
    public int round;

    public enum SwimStyle {
        FREE(0),
        BACKSTROKE(1),
        BREASTSTROKE(2),
        BUTTERFLY(3);

        private int value;
        SwimStyle(int value) {
            this.value=value;
        }

        public int getValue(){
            return value;
        }
    }

    public enum Distance {
        SC50M(0),
        SC100M(1),
        SC200M(2),
        SC400(3),
        LC50M(4),
        LC100M(5),
        LC200M(6),
        LC400M(7);

        private int value;
        Distance(int value) {
            this.value=value;
        }
        public int getValue(){
            return value;
        }
    }

    private int[][] targetTimes;

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
        round++;
    }

    public long getBaseTime() {
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
        targetTimes = new int[4][8];
    }

    public void setLapTime(int time) {
        lapTime=time;
        splitTime.add(time);
    }

    /**
     * get time in 10th of seconds from array of target times
     * @param style specifies swimming style
     * @param distance specifies distance
     * @return target time in 10th of seconds
     */
    public int getTargetTime(SwimStyle style, Distance distance) {
            return targetTimes[style.getValue()][distance.getValue()];
    }

    public void setTargetTime(SwimStyle style, Distance distance, int newTime) {
        targetTimes[style.getValue()][distance.getValue()] = newTime;
    }

    @Override
    public int value() {
        /*
        Hier muss die jeweils benoetigte Zielzeit ausgegeben werden,
        nach der die Teams ausgeglichen werden sollen!
         */
        return -1; // XXX
    }
}
