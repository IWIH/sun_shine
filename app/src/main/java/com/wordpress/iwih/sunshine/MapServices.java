package com.wordpress.iwih.sunshine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by iwih on 26/05/2016.
 */
public class MapServices {

    public static void startMapIntent(Context context, String locationName) {

        String geoUri = "geo:0,0?q=" + locationName.replace(" ", "+");

        Intent geoIntent = new Intent(Intent.ACTION_VIEW);
        geoIntent.setData(Uri.parse(geoUri));

        if (geoIntent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(geoIntent);
            return;
        }

        Log.v(context.getClass().getSimpleName(), "No intent for geo-location. Itent terminated..");
        Toast.makeText(context, "Your device has no Geo-Location App!", Toast.LENGTH_SHORT).show();
    }

}
