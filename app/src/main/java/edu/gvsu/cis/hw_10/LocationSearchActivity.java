package edu.gvsu.cis.hw_10;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationSearchActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    final int PLACE_AUTOCOMPLETE_REQUEST_CODE_0 = 0xACE0;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE_1 = 0xACE1;

    DateTime myDate;
    LocalDate newDate;
    DatePickerDialog datePickerDialog;
    LocationLookup mLocation;

    @BindView(R.id.dateTxt) EditText dateTxt;
    @BindView(R.id.startLocTxt) TextView startLocTxt;
    @BindView(R.id.endLocTxt) TextView endLocTxt;

    @OnClick(R.id.fab) void fab() {
        if(endLocTxt.getText().equals("") || startLocTxt.getText().equals("")) {
            Snackbar.make(findViewById(R.id.locationLayout), "Both locations must be set!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Intent intent = new Intent();
            Parcelable parcel = Parcels.wrap(mLocation);
            intent.putExtra("LOCATION", parcel);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick(R.id.startLocTxt) void startLocTxt() {
        try {
            Intent intent = new
                    PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_0);

        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @OnClick(R.id.endLocTxt) void endLocTxt() {
        try {
            Intent intent = new
                    PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @OnClick(R.id.dateTxt) void dateTxt() {
        datePickerDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_0) {
            String tag = "StartLocation";
            if (resultCode == RESULT_OK) {
                Place pl = PlaceAutocomplete.getPlace(this, data);
                startLocTxt.setText(pl.getAddress());
                LatLng myLatLng = pl.getLatLng();
                mLocation.origLat = myLatLng.latitude;
                mLocation.origLng = myLatLng.longitude;
                Log.i(tag, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status stat = PlaceAutocomplete.getStatus(this, data);
                Log.d(tag, "onActivityResult: ");
            }
            else if (requestCode == RESULT_CANCELED){
                System.out.println("Cancelled by the user");
            }
        }
        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_1) {
            String tag = "EndLocation";
            if (resultCode == RESULT_OK) {
                Place pl = PlaceAutocomplete.getPlace(this, data);
                endLocTxt.setText(pl.getAddress());
                LatLng myLatLng = pl.getLatLng();
                mLocation.endLat = myLatLng.latitude;
                mLocation.endLng = myLatLng.longitude;
                Log.i(tag, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status stat = PlaceAutocomplete.getStatus(this, data);
                Log.d(tag, "onActivityResult: ");
            }
            else if (requestCode == RESULT_CANCELED){
                System.out.println("Cancelled by the user");
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLocation = new LocationLookup();

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDate = new DateTime();
        dateTxt.setText("" + myDate.monthOfYear().getAsShortText() + " " + myDate.getDayOfMonth() + ", " + myDate.getYear());

        datePickerDialog = new DatePickerDialog(
                LocationSearchActivity.this, LocationSearchActivity.this,
                myDate.getYear(), Integer.valueOf(myDate.monthOfYear().get()), myDate.getDayOfMonth());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        // Get the returned month day and year values and update the date text.
        newDate = new LocalDate(i, i1, i2);
        dateTxt.setText("" + newDate.toString("MMM") + " " + i2 + ", " + i);
    }
}
