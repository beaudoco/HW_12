package com.example.collinbeaudoin.hw_4b;

import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.location.Location;

public class GeoCalculator extends AppCompatActivity {

    private class FinalValues {
        // The distance between the coordinates.
        private Float _distance = 0.0f;
        // The bearing between the coordinates.
        private Float _bearing = 0.0f;

        public Float getDistance() {
            return _distance;
        }
        public void setDistance(Float distance) {
            distance = distance / 1000; // Convert to Km.
            _distance = distance;
        }
        public Float getBearing() {
            return _bearing;
        }
        public void setBearing(Float bearing) {
            _bearing = bearing;
        }
    }

    private RelativeLayout relativeLayout;
    private FinalValues finalVals = new FinalValues();

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

        finalVals.setDistance(loc1.distanceTo(loc2));
        finalVals.setBearing(loc1.bearingTo(loc2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_calculator);
        //needs to be fixed
        //relativeLayout = findViewById(R.id.relativeLayout);

        // Main activity GUI elements.
        EditText p1LatTxt = (EditText) findViewById(R.id.p1LatTxt);
        final EditText p2LatTxt = (EditText) findViewById(R.id.p2LatTxt);
        EditText p1LongTxt = (EditText) findViewById(R.id.p1LongTxt);
        final EditText p2LongTxt = (EditText) findViewById(R.id.p2LongTxt);
        Button clrBtn = (Button) findViewById(R.id.clrBtn);
        Button calcBtn = (Button) findViewById(R.id.calcBtn);
        TextView distLbl = (TextView) findViewById(R.id.distLbl);
        TextView bearLbl = (TextView) findViewById(R.id.bearLbl);

        calcBtn.setOnClickListener(v -> {
            String p1Latitude = p1LatTxt.getText().toString();
            String p2Latitude = p2LatTxt.getText().toString();
            String p1Longitude = p1LongTxt.getText().toString();
            String p2Longitude = p2LongTxt.getText().toString();

            // Notify user that there are blank fields.
            if(p1Latitude.length() == 0 || p2Latitude.length() == 0) {
                Snackbar.make(relativeLayout, "Latitude required", Snackbar.LENGTH_LONG)
                        .show();
                return; // No further processing needed.
            } else if(p1Longitude.length() == 0 || p2Longitude.length() == 0) {
                Snackbar.make(relativeLayout, "Longitude required", Snackbar.LENGTH_LONG)
                        .show();
                return; // No further processing needed.
            } else {
                Snackbar.make(relativeLayout, "Distance and Bearing calculated", Snackbar.LENGTH_LONG)
                        .show();
            }

            calculateResults(Double.parseDouble(p1Latitude), Double.parseDouble(p1Longitude),
                    Double.parseDouble(p2Latitude), Double.parseDouble(p2Longitude));

            // Display the results of the calculations
            distLbl.setText(String.format("%s %.2f %s.", "Distance:", finalVals.getDistance(), "kilometers"));
            bearLbl.setText(String.format("%s %.2f %s.", "Bearing:",finalVals.getBearing(), "degrees"));
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
}
