package com.muecke.tkcompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.muecke.tkcompanion.adapter.PersonsAdapter;
import com.muecke.tkcompanion.database.PresenceDataSource;
import com.muecke.tkcompanion.model.Person;
import com.muecke.tkcompanion.model.Team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PresenceActivity extends Activity {

    public static final String ALL = "all";
    final Context context = this;
    int selected=0;
    private String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);

        ListView viewPersons = (ListView) findViewById(R.id.listView_results);
        final List<Person> allPersons = new ArrayList<Person>();
        allPersons.addAll(Team.allPersons);
        final String[] allGroups = grepGroups(allPersons);

        final ArrayAdapter adapter = new PersonsAdapter(this, allPersons);
        session = (DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString());

        PresenceDataSource ds = new PresenceDataSource(this);
        ds.open();
        final List<String> availablePersons = ds.getAllAvailablePersons(session);
        for (Person person : allPersons) {
            person.setPresent(availablePersons.contains(person.getName()));
        }
        ds.close();

        viewPersons.setAdapter(adapter);

        viewPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Person person = (Person) adapterView.getItemAtPosition(position);
                person.setPresent(person.isPresent() ^ true);
                adapter.notifyDataSetChanged();
            }
        });

        final TextView sessionView = (TextView) findViewById(R.id.session_name);
        final TextView locationView = (TextView) findViewById(R.id.session_location);
        final TextView durationView = (TextView) findViewById(R.id.session_duration);
        final TextView groupFilterView = (TextView) findViewById(R.id.filter_group);

        groupFilterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Swim group selector");
                builder.setSingleChoiceItems(allGroups, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //set to buffKey instead of selected
                        //(when cancel not save to selected)
                        selected=which;
                    }
                });
                builder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        allPersons.clear();
                        for (Person person : Team.allPersons) {
                            person.setPresent(false);
                            if (ALL.equals(allGroups[selected]) ||
                                allGroups[selected].equals(person.getGroup())) {

                                allPersons.add(person);
                            }
                        }

                        PresenceDataSource ds = new PresenceDataSource(context);
                        ds.open();
                        final List<String> availablePersons = ds.getAllAvailablePersons(session);
                        ds.close();
                        for (Person person : allPersons) {
                            person.setPresent(availablePersons.contains(person.getName()));
                        }

                        groupFilterView.setText(allGroups[selected]);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                builder.show();

            }
        });

        sessionView.setText(session);

        Button buttonAvail = (Button) findViewById(R.id.button_available);
        buttonAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sessionName = sessionView.getText().toString();
                String locationName = locationView.getText().toString();
                String[] durationSplit = durationView.getText().toString().split(":");
                int duration = Integer.parseInt(durationSplit[0]) * 60 + Integer.parseInt(durationSplit[1]);
                PresenceDataSource ds = new PresenceDataSource(context);
                ds.open();
                for (Person person : allPersons) {
                    if (person.isPresent()) {
                        ds.createPresence(person.getName(), sessionName, locationName);
                    }
                }
                ds.close();
                finish();
            }
        });
    }

    private String[] grepGroups(List<Person> allPersons) {
        Set<String> groups = new HashSet<String>();
        groups.add(ALL);
        for (Person person : allPersons) {
            groups.add(person.getGroup());
        }
        String[] str = new String[groups.size()];
        return groups.toArray(str);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.presence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
