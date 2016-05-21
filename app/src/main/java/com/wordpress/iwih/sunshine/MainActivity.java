package com.wordpress.iwih.sunshine;

import android.app.FragmentManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        Log.v("MainActivity", "Activity Inflated");
    }

    public void refreshData(View view) throws IOException {
        FragmentManager fragManager = getFragmentManager();
        ForecastFragment mainFragement = (ForecastFragment) fragManager.findFragmentById(R.id.forecast_fragment_main);
        mainFragement.fetchWeatherData();
    }

}


