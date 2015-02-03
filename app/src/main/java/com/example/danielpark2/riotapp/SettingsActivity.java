package com.example.danielpark2.riotapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by daniel.park2 on 1/31/15.
 */
public class SettingsActivity extends Activity {

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private int selectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        initRegionSpinner();

    }


    private void initRegionSpinner() {

        spinner = (Spinner) findViewById(R.id.region_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.regions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Log.i("g", "iniitally the selected pos id is " + spinner.getSelectedItemPosition());

        Button selectedButton = (Button) findViewById(R.id.button);
        selectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActResult();
            }
        });
    }


    private void setActResult() {
        if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a region", Toast.LENGTH_LONG);
            return;
        }

        Intent intentMessage = new Intent();
        Log.i("i", "the spinner selected position is" + spinner.getSelectedItemPosition());
        intentMessage.putExtra("itemId", String.valueOf(spinner.getSelectedItemPosition()));
        setResult(2, intentMessage);
        finish();

    }


}

