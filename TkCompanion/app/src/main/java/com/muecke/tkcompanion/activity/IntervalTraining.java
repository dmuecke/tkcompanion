package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.fragment.IntervalTrainingFragment;
import com.muecke.tkcompanion.model.Competition;
import com.muecke.tkcompanion.model.Swimmer;
import com.muecke.tkcompanion.model.WatchStatus;

public class IntervalTraining extends Activity
        implements  IntervalTrainingFragment.InteractionListener {

    private static final int RESULT_SETTINGS = 1;
    public static final String INTERVAL_TRAINING_TAG = "interval_training_tag";
    private IntervalTrainingFragment fragment;


    private WatchStatus timerStatus = WatchStatus.FRESH;
    private android.content.Context context = this;
    private int interval = 40;
    private int selectedCompetition;
    private long chronoBase = 0;
    private long lastElapsed = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_training);
        if (savedInstanceState == null) {
            fragment = IntervalTrainingFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, INTERVAL_TRAINING_TAG)
                    .commit();
        } else {
            fragment = (IntervalTrainingFragment) getFragmentManager().findFragmentByTag(INTERVAL_TRAINING_TAG);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        final TextView countDown = (TextView) findViewById(R.id.count_down);
        countDown.setText(String.format("%02d",interval));

        countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStatus.equals(WatchStatus.RUNNING)) {

                    final AlertDialog.Builder intervalDialog = new AlertDialog.Builder(context);

                    intervalDialog.setTitle("Interval Time");
                    intervalDialog.setMessage("Define send-off time in seconds.");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    input.setText(String.valueOf(interval));
                    intervalDialog.setView(input);

                    intervalDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            interval = Integer.parseInt(input.getText().toString());
                            countDown.setText(String.format("%02d", interval));
                            fragment.setInterval(interval);

                        }
                    });

                    intervalDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    intervalDialog.show();
                }
            }
        });

        final Handler handler = new Handler();
        final Runnable chrono = new Runnable() {
            @Override
            public void run() {
                long millis = SystemClock.elapsedRealtime() - chronoBase;
                final long elapsed = millis / 1000;
                if (elapsed > lastElapsed) {
                    lastElapsed = elapsed;

                    fragment.ChronometerTick( chronoBase);
                    final int countUpTime = (int) (elapsed % interval);
                    final int countDownTime = interval - countUpTime;
                    countDown.setText(String.format("%02d", countDownTime));

                }
                if (timerStatus == WatchStatus.RUNNING) {
                    handler.postDelayed(this, 50);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        };

        final Button startBtn = (Button) findViewById(R.id.start_button);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (timerStatus) {
                    case RUNNING: {
                        startBtn.setText("Reset");
                        timerStatus = WatchStatus.STOPPED;
                        break;

                    }
                    case FRESH: {
                        chronoBase = SystemClock.elapsedRealtime();
                        startBtn.setText("Stop");
                        timerStatus = WatchStatus.RUNNING;
                        lastElapsed = -1;
                        chrono.run();
                        handler.postDelayed(chrono, 100);

                        break;
                    }
                    case STOPPED: {
                        startBtn.setText("Start");
                        timerStatus = WatchStatus.FRESH;
                        countDown.setText(String.format("%02d", interval));
                        break;
                    }
                }
                fragment.timerStatus(timerStatus, context);

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
    protected void onPause() {
        super.onPause();
        timerStatus = WatchStatus.STOPPED;
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
