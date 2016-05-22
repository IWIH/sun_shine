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

    public void v(String msg) {
        Log.v(logTag, msg);
    }

    public void e(String msg) {
        Log.e(logTag, msg);
    }

    public void i(String msg) {
        Log.i(logTag, msg);
    }

    public void w(String msg) {
        Log.w(logTag, msg);
    }

    public void d(String msg) {
        Log.d(logTag, msg);
    }
}
