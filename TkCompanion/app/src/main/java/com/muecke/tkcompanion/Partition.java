package com.muecke.tkcompanion;

import java.util.ArrayList;

/**
* Created by smuecke on 11.09.2014.
*/
public class Partition<E extends Evaluable> {

    private ArrayList<E> teamA, teamB;
    private int[] teamA_v, teamB_v;

    public Partition(ArrayList<E> elements) {
        teamA = new ArrayList<E>();
        teamB = new ArrayList<E>();

        // divide into two teams
        boolean b = true;
        for (E e : elements) {
            if (b) {
                teamA.add(e);
            } else {
                teamB.add(e);
            }
            b = !b;
        }

        assert teamA.size() >= teamB.size() : "Assertion violated: team B is bigger than team A!";

        // create integer arrays containing the elements' values,
        // so that value() doesn't need to be called every time;
        // Coherence of indices must be asserted! (always swap elements simultaneously!)
        teamA_v = new int[teamA.size()];
        teamB_v = new int[teamB.size()];

        for (int i = 0; i < teamA_v.length; i++) {
            teamA_v[i] = teamA.get(i).value();
        }
        for (int i = 0; i < teamB_v.length; i++) {
            teamB_v[i] = teamB.get(i).value();
        }

        optimize();
    }

    /**
     * @return difference of sum of values of team A and team B
     */
    private int calculateImbalance() {
        int sumA = 0;
        int sumB = 0;
        for (int v : teamA_v) sumA += v;
        for (int v : teamB_v) sumB += v;
        return sumA - sumB;
    }

    private void swapTeams() {
        ArrayList<E> c = teamA;
        teamA = teamB;
        teamB = c;

        int[] d = teamA_v;
        teamA_v = teamB_v;
        teamB_v = d;
    }

    /**
     * Swaps two elements of the teams so that the sum of
     * all values of each team is as balanced as possible.
     * Asserts that indices of teamX and teamX_v remain coherent.
     * @return true, iff a change was made to the lists
     */
    private boolean bestSwap() {
        int imb = calculateImbalance();
        int best = Math.abs(imb);
        int current;
        int best_iA = -1;
        int best_iB = -1;

        boolean changed = false;

        // find
        for (int iB = 0; iB < teamB_v.length; iB++) {
            for (int iA = iB; iA < teamA_v.length; iA++) {
                current = Math.abs(imb + 2 * (teamB_v[iB] - teamA_v[iA]));
                if (current < best) {
                    best = current;
                    best_iA = iA;
                    best_iB = iB;
                    changed = true;
                }
            }
        }

        if (!changed) return false; // nothing to be swapped

        // swap in both lists
        int c = teamA_v[best_iA];
        teamA_v[best_iA] = teamB_v[best_iB];
        teamB_v[best_iB] = c;

        E e = teamA.get(best_iA);
        teamA.set(best_iA, teamB.get(best_iB));
        teamB.set(best_iB, e);

        return true;
    }

    /**
     * Finds the optimally balanced 2-partition by value
     * of the given elements
     */
    private void optimize() {
        boolean changed;
        do {
            changed = bestSwap();
        } while (changed);

        // make sure that team A is always equally strong or
        // stronger than team B
        if (calculateImbalance() < 0)
            swapTeams();
    }

    // getters

    public ArrayList<E> getTeamA() {
        return teamA;
    }

    public ArrayList<E> getTeamB() {
        return teamB;
    }

    public String toString() {
        String s = "TEAM A: ";
        for (E e : teamA) { s += e.toString(); }
        s += "\nTEAM B: ";
        for (E e : teamB) { s += e.toString(); }
        s += "\nImbalance: " + calculateImbalance();
        return s;
    }
}