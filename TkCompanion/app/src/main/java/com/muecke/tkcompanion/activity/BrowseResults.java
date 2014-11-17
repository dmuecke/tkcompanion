package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.adapter.ResultsAdapter;
import com.muecke.tkcompanion.database.IntervalResultsDataSource;
import com.muecke.tkcompanion.database.SplitsDataSource;
import com.muecke.tkcompanion.model.Result;

import java.util.ArrayList;
import java.util.List;


public class BrowseResults extends Activity {

    final String[] allFilters = new String[]{"None","by Date","by Person","by Style/Person"};
    private Context context = this;
    private List<Result> results = new ArrayList<Result>();
    int selected=0;
    String selectionArg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_results);

        ListView viewResults = (ListView) findViewById(R.id.listView_results);
        selected=0;
        loadData(allFilters[selected],selectionArg);

        final TextView sessionView = (TextView) findViewById(R.id.filter_selector);
        sessionView.setText("Filter: " + allFilters[selected]);

        final ArrayAdapter adapter = new ResultsAdapter(context, results);
        viewResults.setAdapter(adapter);
        viewResults.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
               final Result r = (Result) adapter.getItem(position);


                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Filter");
                builder.setSingleChoiceItems(allFilters, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //set to buffKey instead of selected
                        //(when cancel not save to selected)
                        selected=which;
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionView.setText("Filter: " + allFilters[selected]);

                        switch (selected) {
                            case 0: { selectionArg=null; loadData(allFilters[selected],selectionArg); break;}
                            case 1: { selectionArg=r.getSession(); loadData(allFilters[selected],selectionArg); break;}
                            case 2: { selectionArg=r.getName(); loadData(allFilters[selected],selectionArg); break;}
                            case 3: {
                                selectionArg=r.getName();
                                loadData(allFilters[2],selectionArg);
                                List<Result> newList = new ArrayList<Result>();
                                for (Result result : results) {
                                    if (r.getCompetition().equals(result.getCompetition())) {
                                        newList.add(result);
                                    }
                                }
                                results.clear();
                                results.addAll(newList);
                                break;}
                        }

                        adapter.notifyDataSetChanged();
                    }
                });


                builder.show();

               return false;
            }
        });



        Button deleteBtn = (Button) findViewById(R.id.button_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteResults(allFilters[selected],selectionArg);

                adapter.notifyDataSetChanged();
            }
        });

    }

    private void loadData(String filter,String arg) {
        results.clear();
        Log.d("loadData", filter + ": " + arg);
        loadFilteredResults(filter, arg);
        loadFilteredIntervals(filter, arg);
    }

    private void deleteResults(String filter, String arg) {
        results.clear();

        SplitsDataSource ds = new SplitsDataSource(context);
        ds.open();
        ds.deleteFilteredData(filter, arg);
        ds.close();
        IntervalResultsDataSource ids = new IntervalResultsDataSource(context);
        ids.open();
        ids.deleteFilteredData(filter, arg);
        ids.close();

    }

    private void loadFilteredResults(String filter, String arg) {
        SplitsDataSource ds = new SplitsDataSource(context);
        ds.open();
        results.addAll(ds.getFilteredData(filter, arg));
        ds.close();
    }

    private void loadFilteredIntervals(String filter, String arg) {
        IntervalResultsDataSource ds = new IntervalResultsDataSource(context);
        ds.open();
        results.addAll(ds.getFilteredData(filter, arg));
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
