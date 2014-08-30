package com.muecke.tkcompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Toast;

import com.muecke.tkcompanion.model.Swimmer;

import java.util.ArrayList;


public class IntervalWatchActivity extends Activity {
    private int interval = 40;
    private int distance = 25;
    private int gap_time = 5;
    private boolean timer_running = false;
    private int lastPosition = -1;
    private ArrayList<Swimmer> swimmerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_watch);
        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);

        Bundle extras = getIntent().getExtras();
        swimmerList = new ArrayList<Swimmer>();
        if (extras != null) {
            interval=extras.getInt("INTERVAL_TIME",40);
            distance=extras.getInt("DISTANCE",25);
            for (String name : extras.getStringArrayList("SWIMMERS")) {
                swimmerList.add(new Swimmer(name));
            }

            gap_time = extras.getInt("GAP_TIME", 5);

        }
        final ListView listView = (ListView) findViewById(R.id.list_swimmer);


        final LazyAdapter adapter = new LazyAdapter(this,swimmerList);

        final Button startBtn = (Button) findViewById(R.id.start_button);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timer_running) {
                    timer_running=true;
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    startBtn.setText("Stop");
                } else {
                    chronometer.stop();
                    startBtn.setText("Start");
                    timer_running=false;

                }


            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chrono) {
                long elapsed = (SystemClock.elapsedRealtime() - chrono.getBase()) / 1000;

                int countDownTime = (int) (interval - elapsed);
                if (countDownTime == 5) {
                    Toast.makeText(getApplicationContext(), "Set!", Toast.LENGTH_SHORT).show();
                }
                if (countDownTime == 2) {
                    Toast.makeText(getApplicationContext(), "Go!", Toast.LENGTH_SHORT).show();
                }
                if (countDownTime == 0) {
                    lastPosition=-1;
                    chrono.setBase(SystemClock.elapsedRealtime());

                } else  if (elapsed % gap_time == 0) {

                    int position = (int) (elapsed / gap_time);

                    if ((lastPosition != position) && (position < swimmerList.size())) {
                        swimmerList.get(position).setBaseTime(SystemClock.elapsedRealtime());
                        lastPosition=position; // avoid double 0
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Swimmer swimmer = (Swimmer) parent.getItemAtPosition(position);

                if (timer_running) {
                    swimmer.setLapTime((int) ((SystemClock.elapsedRealtime() - swimmer.getBaseTime()) / 100));
                    adapter.notifyDataSetChanged();
                    if (position + 1 == swimmerList.size()) {
                        position = -1;
                    }
                    listView.setSelection(position + 1);
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
