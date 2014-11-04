package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.database.SplitsDataSource;
import com.muecke.tkcompanion.model.Competition;

import java.util.ArrayList;
import java.util.List;


public class BrowseResults extends Activity {


    private Context context = this;
    private String session;
    private List<String> results = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_results);

        ListView viewResults = (ListView) findViewById(R.id.listView_results);
        session = "All";
        loadFilteredResults(session);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, results);
        viewResults.setAdapter(adapter);

        final TextView sessionView = (TextView) findViewById(R.id.filter_session);
        sessionView.setText("Select Date: " + session);
        sessionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder sessionFilterDialog = new AlertDialog.Builder(context);

                sessionFilterDialog.setTitle("Session");
                sessionFilterDialog.setMessage("Set Session.");

                // Set an EditText view to get user input
                final EditText sessionFilter = new EditText(context);
                sessionFilter.setText(session);
                sessionFilterDialog.setView(sessionFilter);

                sessionFilterDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        session = sessionFilter.getText().toString();
                        sessionView.setText("Select Date: " + session);

                        loadFilteredResults(session);
                        adapter.notifyDataSetChanged();
                    }
                });

                sessionFilterDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                sessionFilterDialog.show();

            }
        });

        Button deleteBtn = (Button) findViewById(R.id.button_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteResults(session);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void deleteResults(String session) {
        SplitsDataSource ds = new SplitsDataSource(context);
        ds.open();
        results.clear();
        ds.deleteFilteredSplits(session);
        ds.close();

    }

    private void loadFilteredResults(String session) {
        SplitsDataSource ds = new SplitsDataSource(context);
        ds.open();
        results.clear();
        results.addAll(ds.getFilteredSplits(session));
        ds.close();
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
