package com.example.collinbeaudoin.hw_4;

import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GeoCalc_main extends AppCompatActivity {


    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_calc_main);
        relativeLayout = findViewById(R.id.relativeLayout);

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
                Snackbar.make(relativeLayout, R.string.latRequired, Snackbar.LENGTH_LONG)
                        .show();
                return;
            } else if(p1Longitude.length() == 0 || p2Longitude.length() == 0) {
                Snackbar.make(relativeLayout, R.string.longRequired, Snackbar.LENGTH_LONG)
                        .show();
                return;
            } else {
                Snackbar.make(relativeLayout, R.string.calculated, Snackbar.LENGTH_LONG)
                        .show();
                return;
            }
        });
        clrBtn.setOnClickListener(v -> {
            p1LatTxt.setText("");
        });
    }
}
