package com.muecke.tkcompanion;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.muecke.tkcompanion.model.Swimmer;


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
