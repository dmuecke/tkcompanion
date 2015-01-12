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

    public boolean isPredictedTotal() {
        return predictedTotal;
    }

    private boolean predictedTotal;
    public List<Integer> splitTime = new ArrayList<Integer>();

    public Result(String name, int avg, String session, String competition, int total, int poolSize, int intervalLanes) {
        this.name = name;
        this.avg = avg;
        this.session = session;
        this.competition = competition;
        if (total == 0) { // predict total
            Swimming.SwimStyle swimStyle = Competition.parseSwimStyle(competition);
            int dist = Competition.parseDistance(competition).getValue();
            int totalLanes = dist*100 / poolSize;
            int mult = totalLanes / intervalLanes;
            total = avg * mult;
            total -= swimStyle.getStart();
            total += swimStyle.getTurn() * (mult - 1);

            predictedTotal = true;
        } else {
            predictedTotal = false;
        }
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public int getAvg() {
        if (avg == 0 && splitTime.size() > 0) {
            int sumSplit =0;
            for (Integer stime : splitTime) {
                sumSplit += stime;
            }
            avg = sumSplit / splitTime.size();
        }
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

    public String getExport() {
        return String.format("%s;%s;%s;%d;%d;%s",session,competition,name,avg,total,splitTimes());
    }

    private String splitTimes() {
        StringBuilder builder = new StringBuilder();
        boolean delim = false;
        for (Integer split : splitTime) {
            if (delim) {
                builder.append(";");
            } else {
                delim = true;
            }
            builder.append(split);
        }


        return builder.toString();

    }
}
