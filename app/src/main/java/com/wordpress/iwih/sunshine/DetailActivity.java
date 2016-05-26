package com.wordpress.iwih.sunshine;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    private Logger log = new Logger(this.getClass().getSimpleName());
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem shareItem = menu.findItem(R.id.action_detail_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_detail_settings:
                SettingsActivity.startSettingsActivity(this);
                break;
            case R.id.action_detail_share:
                updateAndStartShareActionProvider();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateAndStartShareActionProvider() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
        shareIntent.setType("text/plain");

        setShareActionProviderIntent(shareIntent);

        startActivity(Intent.createChooser(shareIntent, "Share to"));

    }

    private void setShareActionProviderIntent(Intent shareIntent) {
        if (shareActionProvider != null)
            shareActionProvider.setShareIntent(shareIntent);
    }

    private String shareString;

    public void setForecastString(String forecastString) {
        this.shareString = forecastString + " #SunShineApp";
    }
}
