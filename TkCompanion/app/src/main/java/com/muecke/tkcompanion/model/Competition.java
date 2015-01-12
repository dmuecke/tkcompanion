package com.muecke.tkcompanion.model;

import android.util.Log;

import java.io.Serializable;

public class Competition implements Serializable {
    private static Swimming.Distance distance = Swimming.Distance.SC100M;
    private static Swimming.SwimStyle swimStyle = Swimming.SwimStyle.FREE;

    public static int getPoolSize() {
        return poolSize;
    }

    public static void setPoolSize(int poolSize) {
        Competition.poolSize = poolSize;
    }

    private static int poolSize = 25;

    public static String[] allCompetitions = new String[] {"50F", "50B", "50R", "50S", "100F", "100B", "100R", "100S", "100L",
            "200F", "200B", "200R", "200S", "200L", "400F", "400L", "800F", "1500F","4x50L","4x100L","4x50F","4x100F"};

    public static String getShortDesc() {
        return String.format("%s%s",distance.getDesc(),swimStyle.getSortDesc());
    }

    public static void parse(String s) {
        Swimming.SwimStyle newStyle = parseSwimStyle(s);
        Swimming.Distance newDistance = parseDistance(s);
        if (newStyle != null && newDistance != null) {
            swimStyle=newStyle;
            distance=newDistance;
        } else {
            Log.d("parse", "Parse error: " + newDistance);
            Log.d("parse", "Parse error: " + newStyle);

        }
    }

    public static Swimming.Distance parseDistance(String s) {
        return distance.find(s.substring(0, s.length() - 1));
    }

    public static Swimming.SwimStyle parseSwimStyle(String s) {
        return swimStyle.find(s.substring( s.length() - 1, s.length()));
    }

    public static int getSelected() {
        String comp = getShortDesc();
        for (int i = 0; i < allCompetitions.length; i++) {
            if (comp.equals(allCompetitions[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int turnsPerInterval(int avgPerInterval, int poolSize) {
        if (poolSize < 2500) {
            if (avgPerInterval < 250) {
                return 0;
            } else if (avgPerInterval < 380) {
                return 1;
            }
            return 2;
        } else if (poolSize < 5000) {
            if (avgPerInterval < 280) {
                return 0;
            } else if (avgPerInterval < 530){
                return 1;
            }
        } else if (avgPerInterval > 600) {
            return 1;
        }
        return 0;
    }
}
