package com.muecke.tkcompanion.model;

import android.util.Log;

import java.io.Serializable;

public class Competition implements Serializable {
    private static Swimming.Distance distance = Swimming.Distance.SC100M;
    private static Swimming.SwimStyle swimStyle = Swimming.SwimStyle.FREE;

    public static String getShortDesc() {
        return String.format("%d%s",distance.getValue(),swimStyle.getSortDesc());
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

    private static Swimming.Distance parseDistance(String s) {
        return distance.find(s.substring(0, s.length() - 1));
    }

    private static Swimming.SwimStyle parseSwimStyle(String s) {
        return swimStyle.find(s.substring( s.length() - 1, s.length()));
    }
}
