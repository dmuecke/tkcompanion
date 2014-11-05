package com.muecke.tkcompanion.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {
    private String name;
    private int avg;
    private String session;
    private String competition;
    private int total;
    public List<Integer> splitTime = new ArrayList<Integer>();

    public Result(String name, int avg, String session, String competition, int total) {
        this.name = name;
        this.avg = avg;
        this.session = session;
        this.competition = competition;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public int getAvg() {
        return avg;
    }

    public int getTotal() {
        return total;
    }

    public String getSession() {
        return session;
    }

    public String getCompetition() {
        return competition;
    }
}
