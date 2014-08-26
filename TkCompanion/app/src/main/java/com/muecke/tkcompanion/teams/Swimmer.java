package com.muecke.tkcompanion.teams;

import java.util.ArrayList;

/**
 * Created by smuecke on 26.08.2014.
 */
public class Swimmer {
    private String name;
    private int id;
    private ArrayList<Record> personalRecords;

    public Swimmer(String name, int id) {
        this.name = name;
        this.id = id;

        personalRecords = new ArrayList<Record>();
    }

    public Swimmer(String name, int id, ArrayList<Record> records) {
        this.name = name;
        this.id = id;
        personalRecords = records;
    }

    /**
     * adds a new record, given there is either no record yet for the given course
     * or the measures time is better than the current record.
     * @param currentCourse name of the current swimming course
     * @param newTime newly measured time
     * REQUIRES that the course names of all the Records are different
     * ASSURES the same
     */
    public void addTime(String currentCourse, double newTime) {
        Record r = new Record(currentCourse, newTime);
        for (int i = 0; i < personalRecords.size(); i++) {
            if (r.equals(personalRecords.get(i))) {
                if (r.getTime() > personalRecords.get(i).getTime()) {
                    personalRecords.remove(i);
                    personalRecords.add(0, r);
                    return;
                }
            }
        }
        personalRecords.add(0, r);
    }
}
