package com.wordpress.iwih.sunshine;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    final private Logger log = new Logger("MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log.i("Created..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log.v("Activity Inflated");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_main_activity_refresh:
                refreshWeatherData();
                break;
            case R.id.menu_main_activity_show_on_map:
                String location = SettingsActivity.getStringPreferences(this, getString(R.string.pref_location_key), getString(R.string.pref_location_default));
                MapServices.startMapIntent(this, location);
                break;
            case R.id.menu_main_activity_settings:
                SettingsActivity.startSettingsActivity(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshWeatherData() {
        FragmentManager fragManager = getFragmentManager();
        ForecastFragment forecastFragment = (ForecastFragment)
                fragManager.findFragmentById(R.id.forecast_fragment_main);
        forecastFragment.fetchWeatherData();
    }

    @Override
    protected void onStart() {
        log.i("Started..");
        super.onStart();
    }

    @Override
    protected void onStop() {
        log.i("Stopped..");
        super.onStop();
    }

    @Override
    protected void onResume() {
        log.i("Resumed..");
        super.onResume();
    }

    @Override
    protected void onPause() {
        log.i("Paused..");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        log.i("Destroyed..");
        super.onDestroy();
    }
}


