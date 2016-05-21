package com.wordpress.iwih.sunshine;

import android.util.Log;

/**
 * Created by Osama on 21/05/2016.
 */
public class Logger {

    private String logTag;

    public Logger(String logTag) {
        this.logTag = logTag;
    }

    /*
        public void setLogTag(String logTag) {
            this.logTag = logTag;
        }
    */

    public void verbosLog(String msg) {
        Log.v(logTag, msg);
    }

    public void errorLog(String msg) {
        Log.e(logTag, msg);
    }

    public void infoLog(String msg) {
        Log.i(logTag, msg);
    }

    public void warnLog(String msg) {
        Log.w(logTag, msg);
    }

    public void debugLog(String msg) {
        Log.d(logTag, msg);
    }
}
