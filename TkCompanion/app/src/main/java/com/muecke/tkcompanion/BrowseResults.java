package com.muecke.tkcompanion;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.muecke.tkcompanion.database.SplitsDataSource;

import java.util.List;


public class BrowseResults extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_results);

        ListView viewResults = (ListView) findViewById(R.id.listView_results);
        SplitsDataSource ds = new SplitsDataSource(this);
        ds.open();
        List<String> list = ds.getAllSplits();
        ds.close();
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        viewResults.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browse_results, menu);
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
