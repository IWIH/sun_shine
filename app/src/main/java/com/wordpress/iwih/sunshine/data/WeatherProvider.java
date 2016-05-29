package com.wordpress.iwih.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.wordpress.iwih.sunshine.data.WeatherContract.WeatherEntry;
import com.wordpress.iwih.sunshine.data.WeatherContract.LocationEntry;

import android.support.annotation.Nullable;

/**
 * Created by iwih on 29/05/2016.
 */
public class WeatherProvider extends ContentProvider {

    public static final int WEATHER = 100;
    public static final int WEATHER_WITH_LOCATION = 101;
    public static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    public static final int LOCATION = 300;
    public static final int LOCATION_ID = 301;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        final String contentAuthority = WeatherContract.CONTENT_AUTHORITY;
        final String weatherPath = WeatherEntry.PATH_CONTENT;
        final String locationPath = LocationEntry.PATH_CONTENT;

        sUriMatcher.addURI(contentAuthority, weatherPath, WEATHER);
        sUriMatcher.addURI(contentAuthority, weatherPath + "/*", WEATHER_WITH_LOCATION);
        sUriMatcher.addURI(contentAuthority, weatherPath + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);

        sUriMatcher.addURI(contentAuthority, locationPath, LOCATION);
        sUriMatcher.addURI(contentAuthority, locationPath + "/#", LOCATION_ID);

    }

    private SQLiteOpenHelper mOpenHelper;
    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
