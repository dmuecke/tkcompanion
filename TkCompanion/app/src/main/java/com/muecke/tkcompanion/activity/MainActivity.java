package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.database.IntervalResultsDataSource;
import com.muecke.tkcompanion.database.PersonsDataSource;
import com.muecke.tkcompanion.database.PresenceDataSource;
import com.muecke.tkcompanion.database.SplitsDataSource;
import com.muecke.tkcompanion.model.Person;
import com.muecke.tkcompanion.model.Team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;


public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        final boolean auto_presence =prefs.getBoolean("pref_auto_presence", true);
        Log.d("pref auto presence", "" + auto_presence);
        setContentView(R.layout.main);
        findViewById(R.id.button_interval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Team.getTeam().isEmpty()) {
                    String text = auto_presence ? "Create Swimmer first!": "Go to Presence first!";
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                } else {
                    Intent launchactivity = new Intent(MainActivity.this, IntervalTraining.class);
                    startActivity(launchactivity);
                }
            }
        });

        findViewById(R.id.button_presence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Team.allPersons.isEmpty()) {
                    String text = "Create Swimmer first!";
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                } else {
                    Intent launchactivity= new Intent(MainActivity.this,PresenceActivity.class);
                    startActivity(launchactivity);
                }
            }
        });

        View view = findViewById(R.id.presence_layout);
        if (auto_presence) {
            view.setVisibility(View.GONE);
        }
        findViewById(R.id.button_stopwatch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Team.getTeam().isEmpty()) {
                    String text = auto_presence ? "Create Swimmer first!": "Go to Presence first!";

                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                } else {
                    Intent launchactivity = new Intent(MainActivity.this, StopWatch.class);
                    startActivity(launchactivity);
                }
            }
        });


        findViewById(R.id.button_showresults).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchactivity= new Intent(MainActivity.this,BrowseResults.class);
                startActivity(launchactivity);
            }
        });


        File path;
        if (Environment.getExternalStorageState() == null) {
            path = Environment.getDataDirectory();
        } else {
           path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        //Log.d("TAG","path: "+path);
        //Toast.makeText(getApplicationContext(), path.getAbsolutePath(), Toast.LENGTH_LONG).show();
        final File file = new File(path, "persons_import.csv");
        if (file.exists()) {
            final AlertDialog.Builder sendOffDialog = new AlertDialog.Builder(this);

            sendOffDialog.setTitle("Import Persons");
            sendOffDialog.setMessage("Should persons imported from external file?");

            // Set an EditText view to get user input

            sendOffDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        String headerLine = br.readLine();
                        PersonsDataSource ds = new PersonsDataSource(context);
                        ds.open();
                        String[] headers = ds.createTableColumns(headerLine.split(","));
                        while ((line = br.readLine()) != null) {
                            String[] swimValues = line.split(",");
                            ContentValues swimTimes = new ContentValues();
                            for (int i=0; i < swimValues.length; i++) {
                                if (i < 2) {
                                    swimTimes.put(headers[i],swimValues[i]);
                                } else {
                                    int swimValue = Integer.parseInt(swimValues[i]);
                                    swimValue *=10;
                                    swimTimes.put(headers[i], swimValue);
                                }
                            }

                            try {
                                ds.insertPerson(swimTimes);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        ds.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            sendOffDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            sendOffDialog.show();

        }

        IntervalResultsDataSource ds = new IntervalResultsDataSource(context);
        ds.open();
        ds.tidyUp();
        ds.close();
        SplitsDataSource sds = new SplitsDataSource(context);
        sds.open();
        sds.tidyUp();
        sds.close();

        Team.readAllPersonsfromDb(context);
        if (auto_presence) {
            String session = (DateFormat.format("yyyy-MM-dd", new Date()).toString());
            PresenceDataSource pds = new PresenceDataSource(context);
            pds.open();

            for (Person person : Team.allPersons) {
                pds.createPresence(person.getName(), session, "Pool");
                person.setPresent(true);
            }
            pds.close();

        }


    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        Log.d("onSharedPreferenceChanged", key + ": " + preferences.getBoolean(key, true));
        if ("pref_auto_presence".equals(key)) {
            View view = findViewById(R.id.presence_layout);
            boolean auto_presence = preferences.getBoolean(key, true);
            if (auto_presence) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            }

            case R.id.action_add_swimmer: {
                final AlertDialog.Builder sendOffDialog = new AlertDialog.Builder(context);

                sendOffDialog.setTitle("Swimmer");
                sendOffDialog.setMessage("Add a name.");

                // Set an EditText view to get user input
                final EditText sendOffInput = new EditText(context);
                sendOffInput.setText("Swimmer01");
                sendOffDialog.setView(sendOffInput);

                sendOffDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        PersonsDataSource ds = new PersonsDataSource(context);
                        ds.open();
                        Person p = ds.createPerson(sendOffInput.getText().toString(), "ST");
                        ds.close();
                        String session = (DateFormat.format("yyyy-MM-dd", new Date()).toString());
                        PresenceDataSource pds = new PresenceDataSource(context);
                        pds.open();
                        pds.createPresence(p.getName(), session,"Pool");
                        pds.close();
                        p.setPresent(true);
                        Team.allPersons.add(p);
                        Team.team.clear();
                    }
                });

                sendOffDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                sendOffDialog.show();
                break;
            }
        }
        return true;
    }
}
