package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.muecke.tkcompanion.PresenceDialog;
import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.SettingsActivity;
import com.muecke.tkcompanion.StopwatchFragment;
import com.muecke.tkcompanion.model.Competition;
import com.muecke.tkcompanion.model.Swimmer;
import com.muecke.tkcompanion.model.Team;
import com.muecke.tkcompanion.model.WatchStatus;


public class StopWatch extends Activity
    implements PresenceDialog.InteractionListener,
        StopwatchFragment.InteractionListener {
    private static final int RESULT_SETTINGS = 1;
    private StopwatchFragment fragment;
    private int stopwatchMode = 1;


    private WatchStatus timerStatus = WatchStatus.FRESH;
    private android.content.Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        if (savedInstanceState == null) {
            fragment = StopwatchFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        stopwatchMode =  Integer.parseInt(pref.getString("stopwatch_mode","1"));

        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chrono) {

                if (!timerStatus.equals(WatchStatus.RUNNING)) {
                    return;
                }
                fragment.ChronometerTick(stopwatchMode,chrono.getBase());
            }
        });


        final Button startBtn = (Button) findViewById(R.id.start_button);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (timerStatus) {
                    case RUNNING: {
                        chronometer.stop();
                        startBtn.setText("Reset");
                        timerStatus=WatchStatus.STOPPED;
                        Team.saveSplits(context);
                        break;

                    }
                    case FRESH: {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        startBtn.setText("Stop");
                        timerStatus=WatchStatus.RUNNING;
                        chronometer.start();

                        break;
                    }
                    case STOPPED: {
                        startBtn.setText("Start");
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        timerStatus = WatchStatus.FRESH;
                        fragment.SwimmerReset();
                        break;
                    }
                }
                fragment.timerStatus(timerStatus);

            }
        });


        final TextView competition = (TextView) findViewById(R.id.swim_competition);
        competition.setText(Competition.getShortDesc());

        competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStatus.equals(WatchStatus.RUNNING)) {
                    final AlertDialog.Builder sendOffDialog = new AlertDialog.Builder(context);

                    sendOffDialog.setTitle("Competition");
                    sendOffDialog.setMessage("Define competition.");

                    // Set an EditText view to get user input
                    final EditText sendOffInput = new EditText(context);
                    sendOffInput.setText(Competition.getShortDesc());
                    sendOffDialog.setView(sendOffInput);

                    sendOffDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Competition.parse(sendOffInput.getText().toString());
                            competition.setText(Competition.getShortDesc());
                        }
                    });

                    sendOffDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    sendOffDialog.show();

                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stop_watch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent( this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInteraction(Swimmer swimmer) {

    }

    @Override
    public void onPresenceInteraction(boolean[] checkedSwimmers) {

    }
}
