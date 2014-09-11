package com.muecke.tkcompanion.model;

import android.content.Context;
import android.text.format.DateFormat;

import com.muecke.tkcompanion.database.PersonsDataSource;
import com.muecke.tkcompanion.database.PresenceDataSource;
import com.muecke.tkcompanion.database.SplitsDataSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Team implements Serializable {
    public static List<Person> allPersons = new ArrayList<Person>();
    private static final List<Swimmer> team      = new ArrayList<Swimmer>();

    public  static void readAllPersonsfromDb(Context context) {
        PersonsDataSource dataSource = new PersonsDataSource(context);
        dataSource.open();
        allPersons = dataSource.getAllPersons();

        dataSource.close();
        String session = (DateFormat.format("yyyy-MM-dd", new Date()).toString());

        PresenceDataSource ds = new PresenceDataSource(context);
        ds.open();
        final List<String> availablePersons = ds.getAllAvailablePersons(session);
        for (Person person : allPersons) {
            person.setPresent(availablePersons.contains(person.getName()));
        }
        ds.close();

    }

    public static List<Swimmer> getTeam() {
        if (team.isEmpty()) {
            for (Person person : allPersons) {
                if (person.isPresent()) {
                    team.add(new Swimmer(person));
                }
            }
        }

        return team;
    }

    public static void saveSplits(Context context) {
        String session = (DateFormat.format("yyyy-MM-dd", new Date()).toString());

        SplitsDataSource ds = new SplitsDataSource(context);
        ds.open();
        for (Swimmer swimmer : team) {
            ds.createSplitTime(swimmer.getName(), session, Competition.getShortDesc(), swimmer.getTotal(), swimmer.splitTime);
        }


        ds.close();
    }
}
