package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.fragment.StopwatchFragment;
import com.muecke.tkcompanion.model.Competition;
import com.muecke.tkcompanion.model.Swimmer;
import com.muecke.tkcompanion.model.WatchStatus;


public class StopWatch extends Activity
    implements StopwatchFragment.InteractionListener {
    private static final int RESULT_SETTINGS = 1;
    private static final String STOPWATCH_TAG = "stopwatch_tag";
    private StopwatchFragment fragment;


    private WatchStatus timerStatus = WatchStatus.FRESH;
    private android.content.Context context = this;
    private int selectedCompetition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        if (savedInstanceState == null) {
            fragment = StopwatchFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, STOPWATCH_TAG)
                    .commit();
        } else {
            fragment = (StopwatchFragment) getFragmentManager().findFragmentByTag(STOPWATCH_TAG);

        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chrono) {

                if (!timerStatus.equals(WatchStatus.RUNNING)) {
                    return;
                }
                fragment.ChronometerTick(chrono.getBase());
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
                        break;
                    }
                }
                fragment.timerStatus(timerStatus,context);

            }
        });


        final TextView competition = (TextView) findViewById(R.id.swim_competition);
        competition.setText(Competition.getShortDesc());

        competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStatus.equals(WatchStatus.RUNNING)) {
                    final AlertDialog.Builder competitionDialog = new AlertDialog.Builder(context);

                    competitionDialog.setTitle("Filter");
                    competitionDialog.setSingleChoiceItems(Competition.allCompetitions, Competition.getSelected(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //set to buffKey instead of selected
                            //(when cancel not save to selected)
                            selectedCompetition = which;
                        }
                    });
                    competitionDialog.setCancelable(false);
                    competitionDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            Competition.parse(Competition.allCompetitions[selectedCompetition]);
                            competition.setText(Competition.getShortDesc());

                        }
                    });
                    competitionDialog.show();

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

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInteraction(Swimmer swimmer) {

    }

}
