package com.wordpress.iwih.sunshine;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by iwih on 24/05/2016.
 */
public class NetworkUtilities {

    public static boolean isNetworkAvailable(Activity activity, boolean notifyUI) {
        boolean isConnectedToNetwork = isNetworkAvailable(activity);

        if (!isConnectedToNetwork && notifyUI)
            Toast.makeText(activity, "No Internet Connection!", Toast.LENGTH_SHORT).show();

        return isConnectedToNetwork;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return ((networkInfo != null) && (networkInfo.isConnected()));
    }
}
