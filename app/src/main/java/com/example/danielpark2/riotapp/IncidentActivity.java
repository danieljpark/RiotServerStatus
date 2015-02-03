package com.example.danielpark2.riotapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.danielpark2.riotapp.Data.Region;

/**
 * Created by daniel.park2 on 2/2/15.
 */
public class IncidentActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incident_view);
        //data struct setup

        String region = getIntent().getStringExtra("callingRegion");
        String incidentLog = getIntent().getStringExtra("incidents");

        ((TextView) findViewById(R.id.incidents_title)).setText("Incidents Reported for " + Region.getRegionFriendlyName(region));

        if (incidentLog.equals("Last Updated: null")) {
            incidentLog = "None reported.";
        }
        ((TextView) findViewById(R.id.incidents_textView)).setText(incidentLog);

    }

}
