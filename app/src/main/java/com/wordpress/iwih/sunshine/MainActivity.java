package com.wordpress.iwih.sunshine;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("MainActivity", "Activity Inflated");
    }


    public void refreshData(View view) {
        FragmentManager fragManager = getFragmentManager();
        ForecastFragment mainFragement = (ForecastFragment) fragManager.findFragmentById(R.id.forecast_fragment_main);
        mainFragement.fetchWeatherData();
    }

}


