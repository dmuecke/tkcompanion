package com.muecke.tkcompanion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.muecke.tkcompanion.database.PresenceDataSource;
import com.muecke.tkcompanion.database.PersonsDataSource;
import com.muecke.tkcompanion.model.Person;

import java.util.List;


public class PresenceActivity extends Activity {

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);

        ListView viewPersons = (ListView) findViewById(R.id.listView_persons);
        PersonsDataSource dataSource = new PersonsDataSource(this);
        dataSource.open();
        final List<Person> allPersons = dataSource.getAllPersons();
        final ArrayAdapter adapter = new PersonsAdapter(this, allPersons);
        dataSource.close();
        String session = (DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString());

        PresenceDataSource ds = new PresenceDataSource(this);
        ds.open();
        List<String> availablePersons = ds.getAllAvailablePersons(session);
        for (Person person : allPersons) {
            person.setPresent(availablePersons.contains(person.getName()));
        }
        ds.close();

        viewPersons.setAdapter(adapter);

        viewPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Person person = (Person) adapterView.getItemAtPosition(position);
                person.setPresent(person.isPresent() ^ true);
                adapter.notifyDataSetChanged();
            }
        });

        final TextView sessionView = (TextView) findViewById(R.id.session_name);
        final TextView locationView = (TextView) findViewById(R.id.session_location);
        final TextView durationView = (TextView) findViewById(R.id.session_duration);

        sessionView.setText(session);

        Button buttonAvail = (Button) findViewById(R.id.button_available);
        buttonAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sessionName = sessionView.getText().toString();
                String locationName = locationView.getText().toString();
                String[] durationSplit = durationView.getText().toString().split(":");
                int duration = Integer.parseInt(durationSplit[0]) * 60 + Integer.parseInt(durationSplit[1]);
                PresenceDataSource ds = new PresenceDataSource(context);
                ds.open();
                for (Person person : allPersons) {
                    if (person.isPresent()) {
                        ds.createPresence(person.getName(), sessionName, locationName);
                    }
                }
                ds.close();
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.presence, menu);
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

    private class PersonsAdapter extends ArrayAdapter<Person> {
        public PersonsAdapter(Context context, List<Person> list) {
            super(context,R.layout.list_row_presence,list);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = view == null ? inflater.inflate(R.layout.list_row_presence, parent,false) : view;

            Person swimmer = getItem(position);

            final CheckedTextView checkedViewSwimmer = (CheckedTextView) rowView.findViewById(R.id.swim_name);
            checkedViewSwimmer.setChecked(swimmer.isPresent());

            TextView name = (TextView) rowView.findViewById(R.id.swim_name);
            name.setText(swimmer.getName());
            return rowView;
        }
    }
}
