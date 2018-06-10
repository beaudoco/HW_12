package edu.gvsu.cis.hw_10;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HistoryActivity extends AppCompatActivity
    implements HistoryFragment.OnListFragmentInteractionListener {

    public void onListFragmentInteraction(LocationLookup item) {
        System.out.println("Interact!");
        Intent intent = new Intent();
        String[] vals = {String.valueOf(item.origLat), String.valueOf(item.origLng),
                String.valueOf(item.endLat), String.valueOf(item.endLng)};
        intent.putExtra("item", vals);
        setResult(GeoCalculator.HISTORY_RESULT,intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
