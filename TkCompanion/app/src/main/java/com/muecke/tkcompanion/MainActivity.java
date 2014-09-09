package com.muecke.tkcompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.muecke.tkcompanion.database.PersonsDataSource;


public class MainActivity extends Activity {
    private static final int RESULT_SETTINGS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.button_interval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchactivity= new Intent(MainActivity.this,IntervalWatchActivity.class);
                startActivity(launchactivity);
            }
        });

        findViewById(R.id.button_availability).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchactivity= new Intent(MainActivity.this,PresenceActivity.class);
                startActivity(launchactivity);
            }
        });

        PersonsDataSource dataSource = new PersonsDataSource(this);
        dataSource.open();
        if (dataSource.getAllPersons().isEmpty()) { // add some demo data
            for (String s : getResources().getStringArray(R.array.demo_persons)) {
                String[] nameGrup = s.split(",");
                dataSource.createPerson(nameGrup[0], nameGrup[1]);
            }
        }
        dataSource.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent i = new Intent( this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
            }
        }
        return true;
    }
}
