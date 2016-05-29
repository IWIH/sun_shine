package com.wordpress.iwih.sunshine.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by iwih on 28/05/2016.
 */
public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.wordpress.iwih.sunshine";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class LocationEntry implements BaseColumns {

        public static final String PATH_CONTENT = "location";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir" + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item" + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;

        public static final String TABLE_NAME = "location";

        public static final String _ID = "_id";

        public static final String COLUMN_LOCATION_SETTING = "location_setting";

        public static final String COLUMN_CITY_NAME = "city_name";

        public static final String COLUMN_COORD_LONG = "coord_long";

        public static final String COLUMN_COORD_LAT = "coord_lat";

        public static final Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class WeatherEntry implements BaseColumns {

        public static final String PATH_CONTENT = "weather";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir" + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item" + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;

        public static final String TABLE_NAME = "weather";

        public static final String _ID = "_id";

        public static final String COLUMN_LOCATION_KEY = "location_id";

        public static final String COLUMN_DATE_TEXT = "date";

        public static final String COLUMN_WEATHER_ID = "weather_id";

        public static final String COLUMN_SHORT_DESC = "description_short";

        public static final String COLUMN_TEMPERATURE_MIN = "temp_min";

        public static final String COLUMN_TEMPERATURE_MAX = "temp_max";

        public static final String COLUMN_HUMIDITY = "humidity";

        public static final String COLUMN_PRESSURE = "pressure";

        public static final String COLUMN_WIND_SPEED = "wind_speed";

        public static final String COLUMN_WIND_AZIMUTH = "wind_azimuth";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeatherLocation(String locationSetting) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
        }

        public static Uri buildWeatherLocationStartDate(String locationSetting, String dateText) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATE_TEXT, dateText).build();
        }

        public static Uri buildWeatherLocationWithDate(String locationSetting, String dateText) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendPath(dateText).build();
        }

        public static String getLocationSetttingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DATE_TEXT);
        }
    }
}