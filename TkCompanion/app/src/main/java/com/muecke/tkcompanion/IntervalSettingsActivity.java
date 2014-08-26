package com.muecke.tkcompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class IntervalSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_settings);


        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cntSwimmers = ((EditText) findViewById(R.id.count_swimmers)).getText().toString();
                final String intervalTime = ((EditText)findViewById(R.id.interval_time)).getText().toString();
                final String distance = ((EditText)findViewById(R.id.distance)).getText().toString();
                final String gapTime = ((EditText)findViewById(R.id.gap_time)).getText().toString();

                Intent launchactivity= new Intent(IntervalSettingsActivity.this,IntervalWatchActivity.class);
                launchactivity.putExtra("CNT_SWIMMERS", Integer.parseInt(cntSwimmers));
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
