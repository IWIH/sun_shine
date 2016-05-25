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
        switch (itemId){
            case R.id.menu_main_activity_settings:
                SettingsActivity.startSettingsActivity(this);
                break;
            case R.id.menu_main_activity_refresh:
                refreshWeatherData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshData(View view)  {
        refreshWeatherData();
    }

    private void refreshWeatherData() {
        FragmentManager fragManager = getFragmentManager();
        ForecastFragment forecastFragment = (ForecastFragment)
                fragManager.findFragmentById(R.id.forecast_fragment_main);
        forecastFragment.fetchWeatherData();
    }


}


