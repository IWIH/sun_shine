package com.wordpress.iwih.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wordpress.iwih.sunshine.data.WeatherContract.WeatherEntry;
import com.wordpress.iwih.sunshine.data.WeatherContract.LocationEntry;

/**
 * Created by iwih on 28/05/2016.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final String WEATHER_DB_NAME = "weather.db";
    public static final int WEATHER_DB_VERSION = 3;

    public WeatherDbHelper(Context context) {
        super(context, WEATHER_DB_NAME, null, WEATHER_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_LOCATION_TABLE =
                "CREATE TABLE " + LocationEntry.TABLE_NAME + " ( " +
                        LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationEntry.COLUMN_LOCATION_SETTING + " TEXT NOT NULL, " +
                        LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                        LocationEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                        LocationEntry.COLUMN_COORD_LONG + " REAL NOT NULL, " +
                        "UNIQUE (" + LocationEntry.COLUMN_LOCATION_SETTING + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " ( " +
                        WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeatherEntry.COLUMN_LOCATION_KEY + " INTEGER NOT NULL, " +
                        WeatherEntry.COLUMN_DATE_TEXT + " TEXT NOT NULL, " +
                        WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
                        WeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                        WeatherEntry.COLUMN_TEMPERATURE_MAX + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_TEMPERATURE_MIN + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_WIND_AZIMUTH + " REAL NOT NULL, " +
                        "FOREIGN KEY (" + WeatherEntry.COLUMN_LOCATION_KEY + ") REFERENCES " +
                        LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +
                        "UNIQUE (" + WeatherEntry.COLUMN_DATE_TEXT + ", " +
                        WeatherEntry.COLUMN_LOCATION_KEY + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(sqlDropTableQuery(WeatherEntry.TABLE_NAME));
        db.execSQL(sqlDropTableQuery(LocationEntry.TABLE_NAME));

        onCreate(db);
    }

    private String sqlDropTableQuery(String tableName) {
        return ("DROP TABLE IF EXISTS" + tableName + ";");
    }
}
