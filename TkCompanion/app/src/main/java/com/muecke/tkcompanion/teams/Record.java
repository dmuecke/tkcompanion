package com.muecke.tkcompanion.teams;

/**
 * Created by smuecke on 26.08.2014.
 */
public class Record {
    private String courseName;
    private double time;

    public Record(String courseName, double time) {
        this.courseName = courseName;
        this.time = time;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getTime() {
        return time;
    }

    public boolean equals(Record other) {
        return (this.courseName.equals(other.getCourseName()));
    }
}
