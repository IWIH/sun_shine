package com.wordpress.iwih.sunshine;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    final private Logger logger = new Logger("MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logger.v("Activity Inflated");
    }

    public void refreshData(View view) throws IOException {
        FragmentManager fragManager = getFragmentManager();
        ForecastFragment forecastFragment = (ForecastFragment) fragManager.findFragmentById(R.id.forecast_fragment_main);
        forecastFragment.fetchWeatherData();
    }

}


