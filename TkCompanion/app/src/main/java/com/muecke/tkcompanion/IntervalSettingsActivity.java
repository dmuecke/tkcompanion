package com.muecke.tkcompanion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class IntervalSettingsActivity extends Activity {
    final ArrayList<String> list = new ArrayList<String>();

    public class SwimmerAdapter extends ArrayAdapter<String> {

        public SwimmerAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = view == null ? inflater.inflate(R.layout.list_row2, parent,false) : view;
            TextView name = (TextView)rowView.findViewById(R.id.swim_name);
            name.setText(getItem(position));
            TextView textView = (TextView) rowView.findViewById(R.id.swim_position);
            textView.setText(""+(list.indexOf(getItem(position)) + 1));
            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_settings);

        final String[] values = new String[] {"Cosima","Lilly","Julia","Jasmin","Raphael","Martin","Selina","Mats","Anne","Maylin","Diana","Lara","Lotte","Lotta"};

        ListView listView = (ListView) findViewById(R.id.list_names);
        final SwimmerAdapter adapter = new SwimmerAdapter(this, R.layout.list_row2, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int i = list.indexOf(values[position]);
                if (i < 0) {
                    list.add(values[position]);
                } else {
                    list.remove(i);
                }
                adapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String intervalTime = ((EditText)findViewById(R.id.interval_time)).getText().toString();
                final String distance = ((EditText)findViewById(R.id.distance)).getText().toString();
                final String gapTime = ((EditText)findViewById(R.id.gap_time)).getText().toString();

                Intent launchactivity= new Intent(IntervalSettingsActivity.this,IntervalWatchActivity.class);
                launchactivity.putStringArrayListExtra("SWIMMERS", list);
                launchactivity.putExtra("INTERVAL_TIME", Integer.parseInt(intervalTime));
                launchactivity.putExtra("DISTANCE", Integer.parseInt(distance));
                launchactivity.putExtra("GAP_TIME", Integer.parseInt(gapTime));

                startActivity(launchactivity);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interval_settings, menu);
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
