package com.wordpress.iwih.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.wordpress.iwih.sunshine.data.WeatherContract.LocationEntry;
import com.wordpress.iwih.sunshine.data.WeatherContract.WeatherEntry;
import com.wordpress.iwih.sunshine.data.WeatherDbHelper;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    private static ContentValues getLocationValues() {
        ContentValues locationValues = new ContentValues();
        String testLocationSetting = "98530";
        String testCityName = "As Samawah";
        float testCoordLong = 45.29f;
        float testCoordLat = 31.33f;
        locationValues.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        locationValues.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        locationValues.put(LocationEntry.COLUMN_COORD_LONG, testCoordLong);
        locationValues.put(LocationEntry.COLUMN_COORD_LAT, testCoordLat);

        return locationValues;
    }

    private static ContentValues getWeatherValues(long locationRowId) {

        String testDate = "201605028";
        int testWeatherId = 98530;
        String testShortDesc = "rainy!";
        double testTempMin = 23.01;
        double testTempMax = 35.01;
        double testHumidity = 80.01;
        double testPressure = 1.001;
        double testWindSpeed = 20.5;
        double testWindAzimuth = 200.01;

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOCATION_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATE_TEXT, testDate);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, testWeatherId);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, testShortDesc);
        weatherValues.put(WeatherEntry.COLUMN_TEMPERATURE_MAX, testTempMax);
        weatherValues.put(WeatherEntry.COLUMN_TEMPERATURE_MIN, testTempMin);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, testHumidity);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, testPressure);
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, testWindSpeed);
        weatherValues.put(WeatherEntry.COLUMN_WIND_AZIMUTH, testWindAzimuth);

        return weatherValues;
    }

    final String[] locationColumns = {
            LocationEntry._ID,
            LocationEntry.COLUMN_LOCATION_SETTING,
            LocationEntry.COLUMN_CITY_NAME,
            LocationEntry.COLUMN_COORD_LONG,
            LocationEntry.COLUMN_COORD_LAT
    };

    String[] weatherColumns = {
            WeatherEntry.COLUMN_LOCATION_KEY,
            WeatherEntry.COLUMN_DATE_TEXT,
            WeatherEntry.COLUMN_WEATHER_ID,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_TEMPERATURE_MIN,
            WeatherEntry.COLUMN_TEMPERATURE_MAX,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_WIND_AZIMUTH
    };

    public void testReadInsertDb() {


        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();

        ContentValues locationValues = getLocationValues();

        long locationRowId = db.insert(LocationEntry.TABLE_NAME, null, locationValues);

        assertTrue(locationRowId != -1);
        log.v("Inserted location row id = " + locationRowId);


        Cursor locationCursor =
                db.query(LocationEntry.TABLE_NAME, locationColumns, null, null, null, null, null);

        if (locationCursor.moveToFirst()) {
            assertValuesToCursor(locationValues, locationCursor);
        } else {
            fail("No values retrieved from the 'location' table!");
        }

        //now we test 'weather' table
        ContentValues weatherValues = getWeatherValues(locationRowId);

        long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);

        assertTrue(weatherRowId != -1);
        log.v("Inserted weather row id = " + weatherRowId);


        Cursor weatherCursor =
                db.query(WeatherEntry.TABLE_NAME, weatherColumns, null, null, null, null, null);

        if (weatherCursor.moveToFirst()) {

            assertValuesToCursor(weatherValues, weatherCursor);
        } else {
            fail("No values retrieved from 'weather' table!");
        }


    }

    private void assertValuesToCursor(ContentValues expectedValues, Cursor weatherCursor) {
        Set<Map.Entry<String, Object>> expectedValuesSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : expectedValuesSet) {
            String columnName = entry.getKey();
            Object expectedValue = entry.getValue().toString();

            int columnIndex = weatherCursor.getColumnIndex(columnName);
            assertFalse(-1 == columnIndex);
            String sqlDbValue = weatherCursor.getString(columnIndex);

            /*Class objectClass = expectedValue.getClass();
            if (objectClass == float.class || objectClass == double.class){
                expectedValue = Math.round((double)expectedValue);
                sqlDbValue = String.valueOf(Math.round(Double.parseDouble(sqlDbValue)));
            }*/

            assertEquals(expectedValue.toString(), sqlDbValue);
        }
    }
}
