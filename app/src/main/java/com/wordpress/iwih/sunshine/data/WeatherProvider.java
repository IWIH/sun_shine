package com.wordpress.iwih.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.wordpress.iwih.sunshine.data.WeatherContract.WeatherEntry;
import com.wordpress.iwih.sunshine.data.WeatherContract.LocationEntry;

import android.support.annotation.Nullable;
import android.util.Log;

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
    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

    private static final String LOG_TAG = "WeatherProvider";

    static {
        String weatherTableName = WeatherEntry.TABLE_NAME;
        String locationTableName = LocationEntry.TABLE_NAME;

        String innerJoinQuery = weatherTableName + " INNER JOIN " + locationTableName + " ON " +
                weatherTableName + "." + WeatherEntry.COLUMN_LOCATION_KEY + " = " +
                locationTableName + "." + LocationEntry._ID;

        Log.i(LOG_TAG,"Inner join query: "+ innerJoinQuery);
        sWeatherByLocationSettingQueryBuilder.setTables(innerJoinQuery);
    }

    private static final String sLocationSettingSelection =
            LocationEntry.TABLE_NAME + "." + LocationEntry.COLUMN_CITY_NAME + " = ? ";

    private static final String sLocationSettingWithStartDateSelection =
            LocationEntry.TABLE_NAME + "." + LocationEntry.COLUMN_CITY_NAME + " = ? AND" +
                    WeatherEntry.TABLE_NAME + "." + WeatherEntry.COLUMN_DATE_TEXT + " >= ? ";

    private static final String sLocationSettingWithDateSelection =
            LocationEntry.TABLE_NAME + "." + LocationEntry.COLUMN_CITY_NAME + " = ? AND" +
                    WeatherEntry.TABLE_NAME + "." + WeatherEntry.COLUMN_DATE_TEXT + " = ? ";

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int uriMatch = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (uriMatch) {
            //for WeatherEntry
            case WEATHER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case WEATHER_WITH_LOCATION: {
                retCursor = getWeatherByLocationSettingAndStartDate(uri, projection, sortOrder);
                break;
            }
            case WEATHER_WITH_LOCATION_AND_DATE: {
                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                break;
            }
            //for LocationEntry
            case LOCATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case LOCATION_ID: {
                long _id = ContentUris.parseId(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        LocationEntry.TABLE_NAME,
                        projection,
                        LocationEntry._ID + " = '" + _id + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            //Unknown type
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }

        if (retCursor != null)
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    private Cursor getWeatherByLocationSettingAndStartDate(Uri uri, String[] projection, String sortOrder) {

        String weatherLocation = WeatherEntry.getLocationSetttingFromUri(uri);
        String startDate = WeatherEntry.getDateFromUri(uri);

        String[] selectionArgs;
        String selection;
        if (startDate == null) {
            selection = sLocationSettingSelection;
            selectionArgs = new String[]{weatherLocation};
        } else {
            selection = sLocationSettingWithStartDateSelection;
            selectionArgs = new String[]{weatherLocation, startDate};
        }

        return sWeatherByLocationSettingQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getWeatherByLocationSettingAndDate(Uri uri, String[] projection, String sortOrder) {

        String weatherLocation = WeatherEntry.getLocationSetttingFromUri(uri);
        String startDate = WeatherEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;
        if (startDate == null) {
            selection = sLocationSettingSelection;
            selectionArgs = new String[]{weatherLocation};
        } else {
            selection = sLocationSettingWithDateSelection;
            selectionArgs = new String[]{weatherLocation, startDate};
        }

        return sWeatherByLocationSettingQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriMatch = sUriMatcher.match(uri);

        switch (uriMatch) {
            //for WeatherEntry
            case WEATHER:
                return WeatherEntry.CONTENT_TYPE;

            case WEATHER_WITH_LOCATION:
                return WeatherEntry.CONTENT_TYPE;

            case WEATHER_WITH_LOCATION_AND_DATE:
                return WeatherEntry.CONTENT_ITEM_TYPE;

            //for LocationEntry
            case LOCATION:
                return LocationEntry.CONTENT_TYPE;
            case LOCATION_ID:
                return LocationEntry.CONTENT_ITEM_TYPE;

            //Unknown type
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
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
