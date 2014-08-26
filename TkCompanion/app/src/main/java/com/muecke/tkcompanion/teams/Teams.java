package com.muecke.tkcompanion.teams;

import java.util.ArrayList;

/**
 * Created by smuecke on 26.08.2014.
 * Simple class that contains two ArrayLists of Swimmers.
 */
public class Teams {

    private ArrayList<Swimmer> teamA, teamB;

    public Teams(ArrayList<Swimmer> teamA, ArrayList<Swimmer> teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public ArrayList<Swimmer> getTeamA() {
        return teamA;
    }

    public ArrayList<Swimmer> getTeamB() {
        return teamB;
    }
}
