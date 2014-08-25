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
import android.widget.TextView;


public class StopWatchActivity extends Activity {

    private EditText interval;
    private int round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);

        final TextView textRound = (TextView) findViewById(R.id.round);

        interval = (EditText) findViewById(R.id.interval);
        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter adaptor = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adaptor);

        findViewById(R.id.snap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long elapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
                adaptor.add(textRound.getText().toString() + "   " + elapsed/1000 + "." + elapsed%1000);
            }
        });
        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                round = 1;
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                textRound.setText("Round: "+round);
                adaptor.clear();
            }
        });
        findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chrono) {
                long elapsed = (SystemClock.elapsedRealtime() - chrono.getBase()) / 1000;
                int maxTime = Integer.parseInt(interval.getText().toString());
                if (maxTime <= elapsed) {
                    chrono.setBase(SystemClock.elapsedRealtime());
                    round++;
                    textRound.setText("Round: "+round);
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

}
