package com.wordpress.iwih.sunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

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
        shareActionProvider = (ShareActionProvider) shareItem.getActionProvider();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_detail_settings:
                SettingsActivity.startSettingsActivity(this);
                break;
            case R.id.action_detail_share:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateShareActionProvider(String shareStr) {

    }

    private void setShareIntent(Intent intent) {
        if (shareActionProvider != null)
            shareActionProvider.setShareIntent(intent);
    }
}
