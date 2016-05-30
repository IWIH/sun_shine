package com.wordpress.iwih.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.wordpress.iwih.sunshine.data.WeatherContract.LocationEntry;
import com.wordpress.iwih.sunshine.data.WeatherContract.WeatherEntry;
import com.wordpress.iwih.sunshine.data.WeatherDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by iwih on 28/05/2016.
 */
public class TestWeatherProvider extends AndroidTestCase {

    private final Logger log = new Logger(this.getClass().getSimpleName());

    private static final String CITY_NAME_TEST = "As Samawah";
    private static final String LOCATION_SETTING_TEST = "98530";
    private static final String START_DATE_TEST = "201605028";

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.WEATHER_DB_NAME);
    }

    private static ContentValues getLocationValues() {
        ContentValues locationValues = new ContentValues();

        float testCoordLong = 45.29f;
        float testCoordLat = 31.33f;
        locationValues.put(LocationEntry.COLUMN_LOCATION_SETTING, LOCATION_SETTING_TEST);
        locationValues.put(LocationEntry.COLUMN_CITY_NAME, CITY_NAME_TEST);
        locationValues.put(LocationEntry.COLUMN_COORD_LONG, testCoordLong);
        locationValues.put(LocationEntry.COLUMN_COORD_LAT, testCoordLat);

        return locationValues;
    }

    private static ContentValues getWeatherValues(long locationRowId) {

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
        weatherValues.put(WeatherEntry.COLUMN_DATE_TEXT, START_DATE_TEST);
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

    public void testReadInsertDb() {
        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();

        //at first, we test 'location' table
        ContentValues locationValues = getLocationValues();

        long locationRowId = db.insert(LocationEntry.TABLE_NAME, null, locationValues);

        assertTrue(locationRowId != -1);
        log.v("Inserted location row id = " + locationRowId);

        Cursor locationCursor = mContext.getContentResolver()
                .query(LocationEntry.buildLocationUri(locationRowId), null, null, null, null);

        assertTrue(locationCursor != null);
        if (locationCursor.moveToFirst()) {
            assertValuesFromCursor(locationValues, locationCursor);
            locationCursor.close();
        } else {
            fail("No data retrieved from 'location' table!");
        }

        //now we test 'weather' table
        ContentValues weatherValues = getWeatherValues(locationRowId);

        long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);

        assertTrue(weatherRowId != -1);
        log.v("Inserted weather row id = " + weatherRowId);

        Cursor weatherCursor = mContext.getContentResolver()
                .query(WeatherEntry.CONTENT_URI, null, null, null, null);

        assertTrue(weatherCursor != null);

        if (weatherCursor.moveToFirst()) {
            assertValuesFromCursor(weatherValues, weatherCursor);
            weatherCursor.close();
        } else {
            fail("No data retrieved from 'weather' table!");
        }

        weatherCursor = mContext.getContentResolver()
                .query(WeatherEntry.buildWeatherLocation(CITY_NAME_TEST), null, null, null, null);

        assertTrue(weatherCursor != null);

        if (weatherCursor.moveToFirst()) {
            assertValuesFromCursor(weatherValues, weatherCursor);
            weatherCursor.close();
        } else {
            fail("No data retrieved from 'weather' table!");
        }

        weatherCursor = mContext.getContentResolver()
                .query(WeatherEntry.buildWeatherLocationWithDate(CITY_NAME_TEST, START_DATE_TEST), null, null, null, null);

        assertTrue(weatherCursor != null);

        if (weatherCursor.moveToFirst()) {
            assertValuesFromCursor(weatherValues, weatherCursor);
            weatherCursor.close();
        } else {
            fail("No data retrieved from 'weather' table!");
        }
    }

    private void assertValuesFromCursor(ContentValues expectedValues, Cursor dataCursor) {
        Set<Map.Entry<String, Object>> expectedValuesSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : expectedValuesSet) {
            String columnName = entry.getKey();
            Object expectedValue = entry.getValue().toString();

            int columnIndex = dataCursor.getColumnIndex(columnName);
            log.v("columnIndex = " + columnIndex + "; columnName = " + columnName + ";");
            assertFalse(-1 == columnIndex);
            String sqlDbValue = dataCursor.getString(columnIndex);

            assertEquals(expectedValue.toString(), sqlDbValue);
        }
    }
}
