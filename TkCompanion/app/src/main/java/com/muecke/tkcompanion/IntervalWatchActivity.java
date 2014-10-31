package com.muecke.tkcompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muecke.tkcompanion.activity.ListResultsActivity;
import com.muecke.tkcompanion.adapter.StopWatchAdapter;
import com.muecke.tkcompanion.model.Swimmer;
import com.muecke.tkcompanion.model.WatchStatus;

import java.util.ArrayList;


public class IntervalWatchActivity extends Activity {
    private int interval = 40;
    private int distance = 25;
    private int gapTime = 5;
    private ArrayList<Swimmer> swimmerList;


    private WatchStatus timerStatus = WatchStatus.FRESH;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_watch);
        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        swimmerList = new ArrayList<Swimmer>();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean anonymous_training = pref.getBoolean("anonymous_training", true);
        Log.d("onCreate", "anon training: "+anonymous_training);
        if (anonymous_training) {
            swimmerList.add(new Swimmer("Swimmer1"));
        }

        final TextView sendOffView = (TextView) findViewById(R.id.send_off_time);




        sendOffView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStatus.equals(WatchStatus.RUNNING)) {

                    final AlertDialog.Builder sendOffDialog = new AlertDialog.Builder(context);

                    sendOffDialog.setTitle("Send-Off Time");
                    sendOffDialog.setMessage("Define send-off time between swimmers in seconds.");

                    // Set an EditText view to get user input
                    final EditText sendOffInput = new EditText(context);
                    sendOffInput.setText(String.valueOf(gapTime));
                    sendOffDialog.setView(sendOffInput);

                    sendOffDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            gapTime = Integer.parseInt(sendOffInput.getText().toString());
                            sendOffView.setText(String.format("Send-Off: %ds", gapTime));
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
        if (swimmerList.size() < 2) {
            sendOffView.setVisibility(View.INVISIBLE);
        }
        final TextView countDown = (TextView) findViewById(R.id.count_down);
        countDown.setText(String.format("%02d",interval));




        final ListView listView = (ListView) findViewById(R.id.list_swimmer);

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

        final StopWatchAdapter adapter = new StopWatchAdapter(this,swimmerList);


        Button moreSwimmerButon = (Button)findViewById(R.id.button_add_swimmer);
        moreSwimmerButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStatus.equals(WatchStatus.RUNNING)) {
                    swimmerList.add(new Swimmer("Swimmer"+(swimmerList.size()+1)));
                    sendOffView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
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
                        swimmerList.clear();
                        swimmerList.add(new Swimmer("Swimmer1"));
                        sendOffView.setVisibility(View.INVISIBLE);
                        gapTime =5;
                        interval=40;
                        countDown.setText(String.format("%02d", interval));

                        adapter.notifyDataSetChanged();
                        timerStatus = WatchStatus.FRESH;
                        break;
                    }
                }


            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chrono) {
                final long elapsed = (SystemClock.elapsedRealtime() - chrono.getBase()) / 1000;

                if (!timerStatus.equals(WatchStatus.RUNNING)) {
                    return;
                }
                final int countUpTime = (int) (elapsed % interval);
                final int countDownTime = interval - countUpTime;
                if (swimmerList.size() < 4) {
                    if (countDownTime == 5) {
                        Toast.makeText(getApplicationContext(), "Set!", Toast.LENGTH_SHORT).show();
                    }
                    if (countDownTime == 2) {
                        Toast.makeText(getApplicationContext(), "Go!", Toast.LENGTH_SHORT).show();
                    }
                }
                if (countUpTime % gapTime == 0) {
                    boolean update=false;

                    Log.d("OnChronometer", "countUp: "+countUpTime + " elapsed:" + elapsed);

                    for (int position = 0; position < swimmerList.size(); position++) {
                        int pushOff = position * gapTime;
                        int staggeredElapsed = pushOff;
                        if (elapsed > pushOff) {
                            long factor = (elapsed-pushOff) / interval;
                            staggeredElapsed += factor*interval;
                        }
                        if (elapsed == staggeredElapsed) {
                            swimmerList.get(position).pushOff(SystemClock.elapsedRealtime());
                            update=true;
                        }
                    }
                    if (update) {
                        adapter.notifyDataSetChanged();
                    }

                }
                countDown.setText(String.format("%02d", countDownTime));
            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Swimmer swimmer = (Swimmer) parent.getItemAtPosition(position);

                if (timerStatus.equals(WatchStatus.RUNNING)) {

                    swimmer.setLapTime(SystemClock.elapsedRealtime());
                    if (position + 1 == swimmerList.size()) {
                        position = -1;
                    }
                    listView.setSelection(position + 1);
                    adapter.notifyDataSetChanged();
                } else {
                    Intent launchactivity= new Intent(IntervalWatchActivity.this,ListResultsActivity.class);
                    launchactivity.putExtra("SWIMMER", swimmer);
                    startActivity(launchactivity);
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interval_watch, menu);
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
