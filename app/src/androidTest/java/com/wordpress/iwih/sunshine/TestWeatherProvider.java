package com.wordpress.iwih.sunshine;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.wordpress.iwih.sunshine.data.WeatherContract;
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

    public void testReCreateDatabase() {
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


    public void testInsertRead_Weather_Content_Uri() {
        ContentValues valuesWeather = getWeatherValues(1);

        Uri uriInsertedRow = weatherInsertData(valuesWeather);
        assertTrue(uriInsertedRow != null);

        Cursor cursorWeather = getCursorWithoutParameters(uriInsertedRow);

        if (cursorWeather.moveToNext())
            assertValuesFromCursor(valuesWeather, cursorWeather);
        else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedRow);

        cursorWeather.close();
    }

    public void testInsertRead_Weather_withLocation_Uri() {
        ContentValues valuesLocation = getLocationValues();
        Uri uriInsertedLocationRow= locationInsertData(valuesLocation);
        long locationRowId = ContentUris.parseId(uriInsertedLocationRow);

        ContentValues valuesWeather = getWeatherValues(locationRowId);
        Uri uriInsertedWeatherRow = weatherInsertData(valuesWeather);
        assertTrue(uriInsertedWeatherRow != null);

        Cursor cursorWeather = getCursorWithoutParameters(uriInsertedWeatherRow);

        if (cursorWeather.moveToNext())
            assertValuesFromCursor(valuesWeather, cursorWeather);
        else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedWeatherRow);

        cursorWeather.close();
    }

    public void testInsertReadLocationViaUri() {
        ContentValues valuesLocation = getLocationValues();

        Uri uriInsertedRow = locationInsertData(valuesLocation);
        assertTrue(uriInsertedRow != null);

        Cursor cursorLocation = getCursorWithoutParameters(uriInsertedRow);

        if (cursorLocation.moveToNext())
            assertValuesFromCursor(valuesLocation, cursorLocation);
        else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedRow);

        cursorLocation.close();
    }

    private Uri locationInsertData(ContentValues valuesLocation) {
        return mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, valuesLocation);
    }

    private Cursor getCursorWithoutParameters(Uri uriInsertedRow) {
        return mContext.getContentResolver()
                .query(uriInsertedRow, null, null, null, null);
    }

    private Uri weatherInsertData(ContentValues valuesWeather) {
        return mContext.getContentResolver().insert(WeatherEntry.CONTENT_URI, valuesWeather);
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
