package com.muecke.tkcompanion;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.muecke.tkcompanion.model.Swimmer;

import java.util.Collection;
import java.util.Collections;


public class ListResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_results);

        Bundle extras = getIntent().getExtras();
        Swimmer swimmer = null;
        if (extras != null) {
            swimmer= (Swimmer) extras.get("SWIMMER");

        }

        TextView threshold = (TextView) findViewById(R.id.view_threshold);
        if (swimmer.splitTime.size() > 4) {
            Collections.sort(swimmer.splitTime);
            Integer thresholdValue = swimmer.splitTime.get(swimmer.splitTime.size() - 3);
            threshold.setText("Threshold: " + thresholdValue/10 +"." + thresholdValue%10);
        } else {
            threshold.setText("Threshold: n/a");
        }

        TextView average = (TextView) findViewById(R.id.view_average);
        if (swimmer.splitTime.size() > 0) {
            int avg=0;
            for (Integer swimSplit : swimmer.splitTime) {
                avg+=swimSplit;
            }
            avg /= swimmer.splitTime.size();
            average.setText("Average: " + avg/10 + "." + avg%10);

        } else {
            average.setText("Average: n/a");
        }


        final ListView listView = (ListView) findViewById(R.id.list_results);
        ArrayAdapter<String> results = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        for (int splitTime : swimmer.splitTime) {
            results.add("" + splitTime/10 + "." + splitTime%10);
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
