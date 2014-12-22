package com.muecke.tkcompanion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.adapter.ResultsAdapter;
import com.muecke.tkcompanion.database.IntervalResultsDataSource;
import com.muecke.tkcompanion.database.SplitsDataSource;
import com.muecke.tkcompanion.model.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class BrowseResults extends Activity {

    final String[] allFilters = new String[]{"None","by Date","by Person","by Style/Person"};
    private Context context = this;
    private List<Result> results = new ArrayList<Result>();
    int selected=0;
    String selectionArg = null;
    private TextView sessionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_results);

        ListView viewResults = (ListView) findViewById(R.id.listView_results);
        selected=0;
        loadData(allFilters[selected],selectionArg);

        sessionView = (TextView) findViewById(R.id.filter_selector);
        sessionView.setText("Filter: " + allFilters[selected]);

        final ArrayAdapter adapter =  new ResultsAdapter(context, results);
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

               return true;
            }
        });

        viewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Result r = (Result) adapter.getItem(position);
                Intent launchactivity= new Intent(context,ResultDetails.class);
                launchactivity.putExtra("RESULT", r);

                startActivity(launchactivity);

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
        Button exportBtn = (Button) findViewById(R.id.button_export);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path;
                if (Environment.getExternalStorageState() == null) {
                    path = Environment.getDataDirectory();
                } else {
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                }
                final File file = new File(path, "swim_results.csv");
                try {
                    file.createNewFile();
                    FileOutputStream stream = new FileOutputStream(file);
                    OutputStreamWriter writer = new OutputStreamWriter(stream);
                    for (Result result : results) {
                        writer.append(result.getExport());
                        writer.append("\n");
                    }

                    writer.close();
                    stream.close();
                    Toast.makeText(context,"Results exported.", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Can't create export file.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadData(String filter,String arg) {
        results.clear();
        Log.d("loadData", filter + ": " + arg);
        loadFilteredIntervals(filter, arg);
        loadFilteredResults(filter, arg);
    }

    private void deleteResults(String filter, String arg) {
        results.clear();

        IntervalResultsDataSource ids = new IntervalResultsDataSource(context);
        ids.open();
        ids.deleteFilteredData(filter, arg);
        ids.close();
        SplitsDataSource ds = new SplitsDataSource(context);
        ds.open();
        ds.deleteFilteredData(filter, arg);
        ds.close();
        selected=0;
        selectionArg=null;

        loadData(allFilters[selected],selectionArg);
        sessionView.setText("Filter: " + allFilters[selected]);
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
