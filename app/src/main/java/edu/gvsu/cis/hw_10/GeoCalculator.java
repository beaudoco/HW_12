package edu.gvsu.cis.hw_10;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GeoCalculator App
 * @author David Whitters and Collin Beaudoin.
 *
 * Calculates the distance and bearing between two coordinates.
 */

public class GeoCalculator extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    DatabaseReference topRef;

    // Activity Identifier
    public static final int CALC_SETTINGS = 1;
    public static final int HISTORY_RESULT = 2;
    public static final int NEW_COORDINATE_REQUEST = 3;

    public static List<LocationLookup> allHistory;
    LocationLookup mLocation;

    GoogleApiClient apiClient;

    @OnClick(R.id.searchBtn) void searchBtn() {
        Intent intent = new Intent(GeoCalculator.this, LocationSearchActivity.class);
        startActivityForResult(intent, NEW_COORDINATE_REQUEST);
    }

    @Override
    public void onResume() {
        super.onResume();
        topRef = FirebaseDatabase.getInstance().getReference();

        allHistory.clear();
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener (chEvListener);
//topRef.addValueEventListener(valEvListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Hello world!!!!!!");
    }

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

    EditText p1LatTxt;
    EditText p2LatTxt;
    EditText p1LongTxt;
    EditText p2LongTxt;

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

    /**
     * Hide the soft keypad.
     */
    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_calculator);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        allHistory = new ArrayList<LocationLookup>();

        // Setup the api client.
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        // Main activity GUI elements.
        p1LatTxt = (EditText) findViewById(R.id.p1LatTxt);
        p2LatTxt = (EditText) findViewById(R.id.p2LatTxt);
        p1LongTxt = (EditText) findViewById(R.id.p1LongTxt);
        p2LongTxt = (EditText) findViewById(R.id.p2LongTxt);
        Button clrBtn = (Button) findViewById(R.id.clrBtn);
        distLbl = (TextView) findViewById(R.id.distLbl);
        bearLbl = (TextView) findViewById(R.id.bearLbl);
        calcBtn = (Button) findViewById(R.id.calcBtn);
        finalVals = new FinalValues();

        calcBtn.setOnClickListener(v -> {
            hideKeypad(); // Hide the keypad.
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

            // remember the calculation.
            LocationLookup newLocationLookup = new LocationLookup();
            newLocationLookup.origLat = Double.valueOf(p1Latitude);
            newLocationLookup.origLng = Double.valueOf(p1Longitude);
            newLocationLookup.endLat = Double.valueOf(p2Latitude);
            newLocationLookup.endLng = Double.valueOf(p2Longitude);
            newLocationLookup._timeStamp = DateTime.now();

            allHistory.add(newLocationLookup);
        });

        // Clear all inputs when clear button pressed.
        clrBtn.setOnClickListener(v -> {
            hideKeypad(); // Hide the keypad.
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
            } else if(item.getItemId() == R.id.action_history) {
                Intent intent = new Intent(GeoCalculator.this,HistoryActivity.class);
                startActivityForResult(intent, HISTORY_RESULT);
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
        } else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");
            this.p1LatTxt.setText(vals[0]);
            this.p1LongTxt.setText(vals[1]);
            this.p2LatTxt.setText(vals[2]);
            this.p2LongTxt.setText(vals[3]);
        } else if (requestCode == NEW_COORDINATE_REQUEST) {
            if(resultCode == RESULT_OK) {
                Parcelable myParcel = data.getParcelableExtra("LOCATION");
                mLocation = Parcels.unwrap(myParcel);
                this.p1LatTxt.setText(String.valueOf(mLocation.getOrigLat()));
                this.p1LongTxt.setText(String.valueOf(mLocation.getOrigLng()));
                this.p2LatTxt.setText(String.valueOf(mLocation.getEndLat()));
                this.p2LongTxt.setText(String.valueOf(mLocation.getEndLng()));
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                mLocation.set_timeStamp(DateTime.now());
                topRef.push().setValue(mLocation);
            }
        }

        calcBtn.performClick(); // Simulate button click to recalculate bearing and distance.
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);
            entry._key = dataSnapshot.getKey();
            allHistory.add(entry);
        }
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);
            List<LocationLookup> newHistory = new ArrayList<LocationLookup>();
            for (LocationLookup t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allHistory = newHistory;
        }
        @Override

        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        topRef.removeEventListener(chEvListener);
    }
}
