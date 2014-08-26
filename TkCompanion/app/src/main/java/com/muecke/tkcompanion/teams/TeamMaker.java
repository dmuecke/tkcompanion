package com.muecke.tkcompanion.teams;

import java.util.ArrayList;

/**
 * Created by smuecke on 26.08.2014.
 */
public class TeamMaker {

    private ArrayList<Swimmer> allSwimmers, teamA, teamB;
    private String currentCourse;

    public TeamMaker(ArrayList<Swimmer> allSwimmers, String course) {
        this.allSwimmers = allSwimmers;
        int n = allSwimmers.size();

        teamA = (ArrayList<Swimmer>) allSwimmers.subList(0, n/2);
        teamB = (ArrayList<Swimmer>) allSwimmers.subList(n/2, n);

        currentCourse = course;
    }

    /**
     * make best teams for the current course
     */
    public void optimize() {
        // XXX
    }

    /**
     *
     * @param allSwimmers
     * @param course
     * @return
     */
    public static Teams makeTeams(ArrayList<Swimmer> allSwimmers, String course) {
        theTeamMaker = new TeamMaker(allSwimmers, course);
        theTeamMaker.optimize();
        return new Teams(teamA, teamB);
    }
}
