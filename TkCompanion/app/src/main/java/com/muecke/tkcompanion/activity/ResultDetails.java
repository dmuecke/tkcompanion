package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.model.Result;
import com.muecke.tkcompanion.model.Swimmer;

import java.util.Collections;


public class ResultDetails extends Activity {

    private Integer thresholdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_details);

        Bundle extras = getIntent().getExtras();
        Result result = null;
        if (extras != null) {
            result= (Result) extras.get("RESULT");

        }

        TextView competition = (TextView) findViewById(R.id.view_competition);
        competition.setText(result.getCompetition());
        TextView threshold = (TextView) findViewById(R.id.view_threshold);
        if (result.splitTime.size() > 4) {
            Collections.sort(result.splitTime);
            thresholdValue = result.splitTime.get(result.splitTime.size() - 3);
            threshold.setText("Threshold: " + thresholdValue/10 +"." + thresholdValue%10);
        } else {
            thresholdValue = 0;
            threshold.setText("Threshold: n/a");
        }

        Button save = (Button) findViewById(R.id.set_threshold);
        if (thresholdValue == 0) {
            save.setVisibility(View.INVISIBLE);
        } else {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        TextView totalView = (TextView) findViewById(R.id.view_total);
        TextView average = (TextView) findViewById(R.id.view_average);
        if (result.splitTime.size() > 0) {
            int total=0;
            for (Integer swimSplit : result.splitTime) {
                total+=swimSplit;
            }

            int avg = total / result.splitTime.size();
            average.setText("Average: " + avg/10 + "." + avg%10);
            int totmin = total / 600;
            int totsec = total % 600;
            totalView.setText(String.format("Total: %02d:%02d", totmin, totsec/10) + "." + totsec%10);
        } else {
            average.setText("Average: n/a");
            totalView.setText("Total: n/a");
        }


        final ListView listView = (ListView) findViewById(R.id.list_results);
        ArrayAdapter<String> results = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        for (int splitTime : result.splitTime) {
            results.add(Swimmer.formatTime(splitTime));
        }
        listView.setAdapter(results);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_results, menu);
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
