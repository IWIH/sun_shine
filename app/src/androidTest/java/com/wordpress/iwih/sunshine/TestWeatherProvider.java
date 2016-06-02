package com.wordpress.iwih.sunshine;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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

    private static final String TEST_CITY_NAME = "As Samawah";
    private static final String TEST_CITY_NAME_1 = "New York";
    private static final String TEST_LOCATION_SETTING = "98530";
    private static final String TEST_LOCATION_SETTING_1 = "98890";
    private static final String TEST_LOCATION_SETTING_2 = "98930";
    private static final String TEST_START_DATE = "201605028";

    public void testDeleteDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.WEATHER_DB_NAME);
    }

    /**
     * Tests deleting all records in weather table
     */
    public void testDeleteAllWeatherRecord() {
        mContext.getContentResolver().delete(WeatherEntry.CONTENT_URI, null, null);
    }

    /**
     * Tests deleting all records in location table
     */
    public void testDeleteAllLocationRecord() {
        mContext.getContentResolver().delete(WeatherEntry.CONTENT_URI, null, null);
    }

    /**
     * Tests database writing-reading via uri request on WeatherContentProvider.
     * This test uses uri: 'content://com.wordpress.iwih.sunshine/weather'.
     */
    public void testInsertRead_Weather_Content_Uri() {
        ContentValues valuesWeather = getWeatherValues(1, TEST_START_DATE);

        Uri uriInsertedRow = weatherInsertData(valuesWeather);
        assertTrue(uriInsertedRow != null);

        Cursor cursorWeather = getCursorWithoutParameters(uriInsertedRow);

        if (cursorWeather.moveToNext())
            assertValuesFromCursor(valuesWeather, cursorWeather);
        else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedRow);

        cursorWeather.close();
    }

    /**
     * Tests database writing-reading via uri request on WeatherContentProvider.
     * This test uses uri: 'content://com.wordpress.iwih.sunshine/weather/location'.
     */
    public void testInsertRead_Weather_withLocation_Uri() {
        ContentValues valuesLocation = getLocationValues(TEST_LOCATION_SETTING);
        Uri uriInsertedLocationRow = locationInsertData(valuesLocation);
        long locationRowId = ContentUris.parseId(uriInsertedLocationRow);

        ContentValues valuesWeather = getWeatherValues(locationRowId, TEST_START_DATE);
        Uri uriInsertedWeatherRow = weatherInsertData(valuesWeather);
        assertTrue(uriInsertedWeatherRow != null);

        Cursor cursorWeather = getCursorWithoutParameters(uriInsertedWeatherRow);

        if (cursorWeather.moveToNext())
            assertValuesFromCursor(valuesWeather, cursorWeather);
        else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedWeatherRow);

        cursorWeather.close();
    }

    /**
     * Tests database writing-reading via uri request on WeatherContentProvider.
     * This test uses uri: 'content://com.wordpress.iwih.sunshine/weather/location'.
     */
    public void testInsertRead_Weather_withLocationAndStartDate_Uri() {
        //insert dump location data
        ContentValues valuesLocation = getLocationValues(TEST_LOCATION_SETTING);
        Uri uriInsertedLocationRow = locationInsertData(valuesLocation);
        long idLocationRow = ContentUris.parseId(uriInsertedLocationRow);

        //insert dump weather data
        ContentValues valuesWeather = getWeatherValues(idLocationRow, TEST_START_DATE);
        Uri uriInsertedWeatherRow = weatherInsertData(valuesWeather);
        assertTrue(uriInsertedWeatherRow != null);

        //try to retrieve the inserted data
        String dateText = valuesWeather.getAsString(WeatherEntry.COLUMN_DATE_TEXT);
        Uri weatherWithLocationAndStartDateUri = WeatherEntry
                .buildWeatherLocationStartDate(valuesLocation.getAsString(LocationEntry.COLUMN_CITY_NAME), dateText);
        Cursor cursorWeather = getCursorWithoutParameters(weatherWithLocationAndStartDateUri);

        //compare original dump data with the retrieved ones
        if (cursorWeather.moveToNext()) {
            assertValuesFromCursor(valuesWeather, cursorWeather);
        } else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedWeatherRow);

        cursorWeather.close();
    }

    /**
     * Tests database writing-reading via uri request on WeatherContentProvider.
     * This test uses uri: 'content://com.wordpress.iwih.sunshine/weather/location?date='something''.
     */
    public void testInsertRead_Weather_withLocationAndDate_Uri() {
        //insert dump location data
        ContentValues valuesLocation = getLocationValues(TEST_LOCATION_SETTING);
        Uri uriInsertedLocationRow = locationInsertData(valuesLocation);
        long idLocationRow = ContentUris.parseId(uriInsertedLocationRow);

        //insert dump weather data
        ContentValues valuesWeather = getWeatherValues(idLocationRow, TEST_START_DATE);
        Uri uriInsertedWeatherRow = weatherInsertData(valuesWeather);
        assertTrue(uriInsertedWeatherRow != null);

        //try to retrieve the inserted data
        String dateText = valuesWeather.getAsString(WeatherEntry.COLUMN_DATE_TEXT);
        Uri weatherWithLocationAndStartDateUri = WeatherEntry
                .buildWeatherLocationWithDate(valuesLocation.getAsString(LocationEntry.COLUMN_CITY_NAME), dateText);
        Cursor cursorWeather = getCursorWithoutParameters(weatherWithLocationAndStartDateUri);

        //compare original dump data with the retrieved ones
        if (cursorWeather.moveToNext()) {
            assertValuesFromCursor(valuesWeather, cursorWeather);
            assertValuesFromCursor(valuesLocation, cursorWeather);
        } else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedWeatherRow);

        cursorWeather.close();
    }

    /**
     * Tests database writing-reading via uri request on WeatherContentProvider.
     * This test uses uri: 'content://com.wordpress.iwih.sunshine/location'.
     */
    public void testInsertReadLocationViaUri() {
        ContentValues valuesLocation = getLocationValues(TEST_LOCATION_SETTING);

        Uri uriInsertedRow = locationInsertData(valuesLocation);
        assertTrue(uriInsertedRow != null);

        Cursor cursorLocation = getCursorWithoutParameters(uriInsertedRow);

        if (cursorLocation.moveToNext())
            assertValuesFromCursor(valuesLocation, cursorLocation);
        else
            fail("Couldn't insert data and read it again properly, uri: " + uriInsertedRow);

        cursorLocation.close();
    }

    /**
     * Tests database writing-reading via uri request on WeatherContentProvider.
     * This test uses uri: 'content://com.wordpress.iwih.sunshine/location'.
     */
    public void testUpdateLocation() {
        //insert data
        ContentValues valuesLocation1 = getLocationValues(TEST_LOCATION_SETTING_1);
        Uri uriInsertedRow = locationInsertData(valuesLocation1);
        assertTrue(uriInsertedRow != null);
        long idInsertedRow = ContentUris.parseId(uriInsertedRow);

        //update data
        ContentValues valuesLocation2 = new ContentValues();
        valuesLocation2.put(LocationEntry.COLUMN_CITY_NAME, TEST_CITY_NAME_1);
        int rowsUpdated = mContext.getContentResolver()
                .update(LocationEntry.CONTENT_URI,
                        valuesLocation2,
                        LocationEntry._ID + " = ?",
                        new String[]{String.valueOf(idInsertedRow)});
        assertTrue(rowsUpdated > 0);

        //read data and compare it
        Cursor cursorLocation = getCursorWithoutParameters(uriInsertedRow);

        if (cursorLocation.moveToNext())
            assertValuesFromCursor(valuesLocation2, cursorLocation);
        else
            fail("Couldn't insert data, update and read it again properly, uri: " + uriInsertedRow);

        cursorLocation.close();
    }

    /**
     * Provides dump data for testing purpose.
     */
    private static ContentValues getLocationValues(String locationSetting) {
        ContentValues locationValues = new ContentValues();

        float testCoordLong = 45.29f;
        float testCoordLat = 31.33f;
        locationValues.put(LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
        locationValues.put(LocationEntry.COLUMN_CITY_NAME, TEST_CITY_NAME);
        locationValues.put(LocationEntry.COLUMN_COORD_LONG, testCoordLong);
        locationValues.put(LocationEntry.COLUMN_COORD_LAT, testCoordLat);

        return locationValues;
    }

    /**
     * Provides dump data for testing purpose.
     *
     * @param locationRowId Prefered location id to be included in the data.
     */
    private static ContentValues getWeatherValues(long locationRowId, String date) {

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
        weatherValues.put(WeatherEntry.COLUMN_DATE_TEXT, date);
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
