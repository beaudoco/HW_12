package com.example.collinbeaudoin.hw_4b;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;

public class GeoCalculator extends AppCompatActivity {

    // Activity Identifier
    public static final int CALC_SETTINGS = 1;

    /**
     * Used to store the calculated values and the units. Provides getters and setters for all values.
     */
    private class FinalValues {
        // The distance between the coordinates.
        private Float _distance = 0.0f;
        // The bearing between the coordinates.
        private Float _bearing = 0.0f;
        // The units that the distance is in.
        private String _distUnits = getString(R.string.kilometers);
        // The units that the bearing is in.
        private String _bearUnits = getString(R.string.degrees);

        public Float getDistance() {
            return _distance;
        }
        public void setDistance(Float distInMeters) {
            if(_distUnits.equalsIgnoreCase(getString(R.string.kilometers))) {
                _distance = distInMeters / 1000; // Convert to Km.
            } else if(_distUnits.equalsIgnoreCase(getString(R.string.miles))) {
                _distance = distInMeters / 1609.34f; // Convert to miles.
            }
        }
        public Float getBearing() {
            return _bearing;
        }
        public void setBearing(Float bearInDegrees) {
            if(_bearUnits.equalsIgnoreCase(getString(R.string.degrees))) {
                _bearing = bearInDegrees;
            } else if(_bearUnits.equalsIgnoreCase(getString(R.string.mils))) {
                _bearing = (bearInDegrees / 0.05625f); // Convert to mils.
            }
        }
        public void setDistUnits(String distUnits) {
            _distUnits = distUnits;
        }
        public void setBearUnits(String bearUnits) {
            _bearUnits = bearUnits;
        }
        public String getBearUnits() { return _bearUnits; }
        public String getDistUnits() { return _distUnits; }
    }

    private CoordinatorLayout coordinatorLayout;
    private FinalValues finalVals;

    private TextView distLbl;
    private TextView bearLbl;
    private Button calcBtn;

    /**
     * Displays the final values in the appropriate text views.
     */
    private void displayResults() {
        // Display the results of the calculations
        distLbl.setText(String.format("%s %.2f %s.", getString(R.string.distance), finalVals.getDistance(), finalVals.getDistUnits()));
        bearLbl.setText(String.format("%s %.2f %s.", getString(R.string.bearing),finalVals.getBearing(), finalVals.getBearUnits()));
    }

    /**
     * Calculates the distance and bearing between two coordinates and stores the results in a
     * FinalValues object.
     *
     * @param p1Lat
     *      The latitude of the first coordinate.
     * @param p1Long
     *      The longitude of the first coordinate.
     * @param p2Lat
     *      The latitude of the second coordinate.
     * @param p2Long
     *      The longitude of the second coordinate.
     */
    private void calculateResults(Double p1Lat, Double p1Long, Double p2Lat, Double p2Long) {
        // Found on stack overflow: https://stackoverflow.com/questions/2741403/get-the-distance-between-two-geo-points
        Location loc1 = new Location("");
        loc1.setLatitude(p1Lat);
        loc1.setLongitude(p1Long);

        Location loc2 = new Location("");
        loc2.setLatitude(p2Lat);
        loc2.setLongitude(p2Long);

        float dist = loc1.distanceTo(loc2);

        finalVals.setDistance(loc1.distanceTo(loc2));
        finalVals.setBearing(loc1.bearingTo(loc2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_calculator);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Main activity GUI elements.
        EditText p1LatTxt = (EditText) findViewById(R.id.p1LatTxt);
        final EditText p2LatTxt = (EditText) findViewById(R.id.p2LatTxt);
        EditText p1LongTxt = (EditText) findViewById(R.id.p1LongTxt);
        final EditText p2LongTxt = (EditText) findViewById(R.id.p2LongTxt);
        Button clrBtn = (Button) findViewById(R.id.clrBtn);
        distLbl = (TextView) findViewById(R.id.distLbl);
        bearLbl = (TextView) findViewById(R.id.bearLbl);
        calcBtn = (Button) findViewById(R.id.calcBtn);
        finalVals = new FinalValues();

        calcBtn.setOnClickListener(v -> {
            String p1Latitude = p1LatTxt.getText().toString();
            String p2Latitude = p2LatTxt.getText().toString();
            String p1Longitude = p1LongTxt.getText().toString();
            String p2Longitude = p2LongTxt.getText().toString();

            // Notify user that there are blank fields.
            if(p1Latitude.length() == 0 || p2Latitude.length() == 0) {
                Snackbar.make(coordinatorLayout, R.string.latRequired, Snackbar.LENGTH_LONG)
                        .show();
                return; // No further processing needed.
            } else if(p1Longitude.length() == 0 || p2Longitude.length() == 0) {
                Snackbar.make(coordinatorLayout, R.string.longRequired, Snackbar.LENGTH_LONG)
                        .show();
                return; // No further processing needed.
            } else {
                Snackbar.make(coordinatorLayout, R.string.calculated, Snackbar.LENGTH_LONG)
                        .show();
            }

            calculateResults(Double.parseDouble(p1Latitude), Double.parseDouble(p1Longitude),
                    Double.parseDouble(p2Latitude), Double.parseDouble(p2Longitude));

            displayResults();
        });

        // Clear all inputs when clear button pressed.
        clrBtn.setOnClickListener(v -> {
            p1LatTxt.getText().clear();
            p2LatTxt.getText().clear();
            p1LongTxt.getText().clear();
            p2LongTxt.getText().clear();
            bearLbl.setText("Bearing:");
            distLbl.setText("Distance:");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geo_calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        // If the super class doesn't handle the selection, we will.
        if(!handled) {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                Intent intent = new Intent(GeoCalculator.this, Settings.class);
                startActivityForResult(intent, CALC_SETTINGS);
                handled = true;
            }
        }

        return handled;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == CALC_SETTINGS) {
            finalVals.setBearUnits(data.getStringExtra("bearUnit"));
            finalVals.setDistUnits(data.getStringExtra("distUnit"));
        }
        calcBtn.performClick(); // Simulate button click to recalculate bearing and distance.
    }
}
