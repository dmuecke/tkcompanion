package com.muecke.tkcompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.muecke.tkcompanion.database.PersonsDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MainActivity extends Activity {
    private static final int RESULT_SETTINGS = 1;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.button_interval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchactivity= new Intent(MainActivity.this,IntervalWatchActivity.class);
                startActivity(launchactivity);
            }
        });

        findViewById(R.id.button_availability).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchactivity= new Intent(MainActivity.this,PresenceActivity.class);
                startActivity(launchactivity);
            }
        });

        final File file = new File("/data/Downloads", "persons_import.csv");
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

//        PersonsDataSource dataSource = new PersonsDataSource(this);
//        dataSource.open();
//        if (dataSource.getAllPersons().isEmpty()) { // add some demo data
//            for (String s : getResources().getStringArray(R.array.demo_persons)) {
//                String[] nameGrup = s.split(",");
//                dataSource.createPerson(nameGrup[0], nameGrup[1]);
//            }
//        }
//        dataSource.close();

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
                Intent i = new Intent( this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
            }
        }
        return true;
    }
}
