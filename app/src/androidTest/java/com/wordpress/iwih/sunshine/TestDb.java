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

        String testDate = "201605028";
        int testWeatherId = 98530;
        String testShortDesc = "rainy!";
        double testTempMin = 23d;
        double testTempMax = 35d;
        double testHumidity = 80d;
        double testPressure = 1.00d;
        double testWindSpeed = 20.5d;
        double testWindAzimuth = 200d;

        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();

        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        locationValues.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        locationValues.put(LocationEntry.COLUMN_COORD_LONG, testCoordLong);
        locationValues.put(LocationEntry.COLUMN_COORD_LAT, testCoordLat);

        long locationRowId = db.insert(LocationEntry.TABLE_NAME, null, locationValues);

        assertTrue(locationRowId != -1);
        log.v("Inserted location row id = " + locationRowId);

        String[] locationColumns = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCATION_SETTING,
                LocationEntry.COLUMN_CITY_NAME,
                LocationEntry.COLUMN_COORD_LONG,
                LocationEntry.COLUMN_COORD_LAT
        };

        Cursor locationCursor = db.query(LocationEntry.TABLE_NAME,
                locationColumns,
                null,
                null,
                null,
                null,
                null);

        if (locationCursor.moveToFirst()) {
            int locationSettingIndex = locationCursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String locationSetting = locationCursor.getString(locationSettingIndex);

            int cityNameIndex = locationCursor.getColumnIndex(LocationEntry.COLUMN_CITY_NAME);
            String cityName = locationCursor.getString(cityNameIndex);

            int coordLongIndex = locationCursor.getColumnIndex(LocationEntry.COLUMN_COORD_LONG);
            double coordLong = locationCursor.getDouble(coordLongIndex);

            int coordLatIndex = locationCursor.getColumnIndex(LocationEntry.COLUMN_COORD_LAT);
            double coordLat = locationCursor.getDouble(coordLatIndex);

            locationCursor.close();

            assertEquals(locationSetting, testLocationSetting);
            assertEquals(cityName, testCityName);
            assertEquals(coordLong, testCoordLong);
            assertEquals(coordLat, testCoordLat);
        } else {
            fail("No values retrieved from the 'location' table!");
        }

        //now we test 'weather' table
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

        long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);

        assertTrue(weatherRowId != -1);
        log.v("Inserted weather row id = " + weatherRowId);

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

        Cursor weatherCursor = db.query(WeatherEntry.TABLE_NAME,
                weatherColumns,
                null,
                null,
                null,
                null,
                null);

        if (weatherCursor.moveToFirst()) {

            int dateIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_DATE_TEXT);
            String date = weatherCursor.getString(dateIndex);

            int weatherIdIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID);
            int weatherId = weatherCursor.getInt(weatherIdIndex);

            int shortDescIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC);
            String shortDesc = weatherCursor.getString(shortDescIndex);

            int tempMinIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_TEMPERATURE_MIN);
            double tempMin = weatherCursor.getFloat(tempMinIndex);

            int tempMaxIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_TEMPERATURE_MAX);
            double tempMax = weatherCursor.getFloat(tempMaxIndex);

            int humidityIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY);
            double humidity = weatherCursor.getFloat(humidityIndex);

            int pressureIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE);
            double pressure = weatherCursor.getFloat(pressureIndex);

            int windSpeedIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED);
            double windSpeed = weatherCursor.getFloat(windSpeedIndex);

            int windAzimuthIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WIND_AZIMUTH);
            double windAzimuth = weatherCursor.getFloat(windAzimuthIndex);

            weatherCursor.close();

            assertEquals(date, testDate);
            assertEquals(weatherId, testWeatherId);
            assertEquals(shortDesc, testShortDesc);
            assertEquals(tempMin, testTempMin);
            assertEquals(tempMax, testTempMax);
            assertEquals(humidity, testHumidity);
            assertEquals(pressure, testPressure);
            assertEquals(windSpeed, testWindSpeed);
            assertEquals(windAzimuth, testWindAzimuth);
        } else {
            fail("No values retrieved from 'weather' table!");
        }


    }
}
