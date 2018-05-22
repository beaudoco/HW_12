package com.example.collinbeaudoin.hw_4b;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {

    private String bearSelection;
    private String distSelection;

    private class SpinnerItemSelectedListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(adapterView.getId() == R.id.distSpinner) {
                distSelection = (String) adapterView.getItemAtPosition(i);
            } else if(adapterView.getId() == R.id.bearSpinner) {
                bearSelection = (String) adapterView.getItemAtPosition(i);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private SpinnerItemSelectedListener onItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        onItemSelectedListener = new SpinnerItemSelectedListener();
        bearSelection = getString(R.string.degrees);
        distSelection = getString(R.string.kilometers);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("distUnit", distSelection);
                intent.putExtra("bearUnit", bearSelection);
                setResult(GeoCalculator.CALC_SETTINGS, intent);
                finish();
            }
        });

        Spinner distSpinner = (Spinner) findViewById(R.id.distSpinner);
        Spinner bearSpinner = (Spinner) findViewById(R.id.bearSpinner);

        ArrayAdapter<CharSequence> distAdapter = ArrayAdapter.createFromResource(this,
                R.array.distanceUnits, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> bearAdapter = ArrayAdapter.createFromResource(this,
                R.array.bearingUnits, android.R.layout.simple_spinner_item);

        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distSpinner.setAdapter(distAdapter);
        distSpinner.setOnItemSelectedListener(onItemSelectedListener);

        bearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bearSpinner.setAdapter(bearAdapter);
        bearSpinner.setOnItemSelectedListener(onItemSelectedListener);
    }
}
