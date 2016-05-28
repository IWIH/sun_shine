package com.wordpress.iwih.sunshine;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.wordpress.iwih.sunshine.data.WeatherDbHelper;

/**
 * Created by iwih on 28/05/2016.
 */
public class TestDb extends AndroidTestCase {

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.WEATHER_DB_NAME);

        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());
        db.close();
    }
}
