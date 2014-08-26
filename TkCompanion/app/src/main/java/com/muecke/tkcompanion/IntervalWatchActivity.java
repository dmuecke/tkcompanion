package com.muecke.tkcompanion;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class IntervalWatchActivity extends Activity {
    private int interval = 40;
    private int distance = 25;
    private int swimmers = 1;
    private int gap_time = 5;
    private int snap_round = 1;

    private int round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_watch);
        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);


        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter adaptor = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adaptor);

        final Button snapBtn = (Button) findViewById(R.id.snap_button);
        final Button stpBtn = (Button) findViewById(R.id.stop_button);
        final Button startBtn = (Button) findViewById(R.id.start_button);

        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long elapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
                long seconds = elapsed / 1000;
                seconds -= (snap_round-1)*gap_time;

                adaptor.add("Round " + round + "  Swimmer" + snap_round + "    " + seconds + "." + elapsed % 1000);
                if (swimmers > snap_round) {
                    snap_round++;
                } else {
                    snapBtn.setEnabled(false);
                }
                snapBtn.setText("Snap Round " + round + " Swimmer" + snap_round);

            }
        });

        stpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chronometer.stop();
                snapBtn.setEnabled(false);
                stpBtn.setEnabled(false);
                startBtn.setEnabled(true);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                round = 1;
                snap_round =1;
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                snapBtn.setText("Snap Round " + round + " Swimmer" + snap_round);

                adaptor.clear();
                snapBtn.setEnabled(true);
                stpBtn.setEnabled(true);
                startBtn.setEnabled(false);
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chrono) {
                long elapsed = (SystemClock.elapsedRealtime() - chrono.getBase()) / 1000;
                int maxTime = interval;
                if (maxTime <= elapsed) {
                    chrono.setBase(SystemClock.elapsedRealtime());
                    round++;
                    snap_round = 1;

                    snapBtn.setText("Snap Round " + round + " Swimmer" + snap_round);
                    snapBtn.setEnabled(true);
                }
                int countDownTime = (int) (maxTime - elapsed);
                if (countDownTime > 4 && countDownTime <= 5) {
                    Toast.makeText(getApplicationContext(), "Set!", Toast.LENGTH_SHORT).show();
                }
                if (countDownTime > 1 && countDownTime <= 2) {
                    Toast.makeText(getApplicationContext(), "Go!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            interval=extras.getInt("INTERVAL_TIME",40);
            distance=extras.getInt("DISTANCE",25);
            swimmers=extras.getInt("CNT_SWIMMERS",25);
            gap_time = extras.getInt("GAP_TIME", 5);

        }
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
