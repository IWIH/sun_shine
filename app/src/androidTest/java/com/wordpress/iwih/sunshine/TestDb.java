package com.wordpress.iwih.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.wordpress.iwih.sunshine.data.WeatherContract.LocationEntry;
import com.wordpress.iwih.sunshine.data.WeatherContract.WeatherEntry;
import com.wordpress.iwih.sunshine.data.WeatherDbHelper;

/**
 * Created by iwih on 28/05/2016.
 */
public class TestDb extends AndroidTestCase {

    private final Logger log = new Logger(this.getClass().getSimpleName());

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.WEATHER_DB_NAME);

        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testReadInsertDb() {

        String testLocationSetting = "98530";
        String testCityName = "As Samawah";
        double testCoordLong = 45.294399;
        double testCoordLat = 31.33198;

        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        content.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        content.put(LocationEntry.COLUMN_COORD_LONG, testCoordLong);
        content.put(LocationEntry.COLUMN_COORD_LAT, testCoordLat);

        long locationRowId = db.insert(LocationEntry.TABLE_NAME, null, content);

        assertTrue(locationRowId != -1);
        log.v("Inserted row id = " + locationRowId);

        String[] columns = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCATION_SETTING,
                LocationEntry.COLUMN_CITY_NAME,
                LocationEntry.COLUMN_COORD_LONG,
                LocationEntry.COLUMN_COORD_LAT
        };

        Cursor cursor = db.query(LocationEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            int locationSettingIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String locationSetting = cursor.getString(locationSettingIndex);

            int cityNameIndex = cursor.getColumnIndex(LocationEntry.COLUMN_CITY_NAME);
            String cityName = cursor.getString(cityNameIndex);

            int coordLongIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LONG);
            double coordLong = cursor.getDouble(coordLongIndex);

            int coordLatIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LAT);
            double coordLat = cursor.getDouble(coordLatIndex);

            assertEquals(locationSetting, testLocationSetting);
            assertEquals(cityName, testCityName);
            assertEquals(coordLong, testCoordLong);
            assertEquals(coordLat, testCoordLat);
        } else{
            fail("No values retrieved from the database!");
        }


    }
}
