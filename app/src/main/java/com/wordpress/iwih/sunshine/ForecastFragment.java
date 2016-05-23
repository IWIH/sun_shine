package com.wordpress.iwih.sunshine;


import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    final Logger log = new Logger(this.getClass().getSimpleName());
    public ArrayAdapter<String> forecastArrayAdapter;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.forecast_fragment, container, false);
        log.v("Fragment Inflated!");

        forecastArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.forecast_list_item_fragment,
                R.id.forecast_text_view,
                Arrays.asList(new String[]{""}));

        ListView forecastListView = (ListView) inflate.findViewById(R.id.forecast_list_view);
        forecastListView.setAdapter(forecastArrayAdapter);

        fetchWeatherData();

        return inflate;

    }

    public void fetchWeatherData() {
        log.v("Calling FetchWeatherAsync...");
        new FetchWeatherAsync().execute("As Samawah", "json", "metric", "7");
    }

    public class FetchWeatherAsync extends AsyncTask<String, Void, Void> {

        private String cityName;
        private String dataMode;
        private String dataUnits;
        private String daysCount;

        @Override
        protected Void doInBackground(String... params) {

            log.i(params.length + "x object(s) passed to FetchWeatherAsync worker.");

            cityName = params[0];
            dataMode = params[1];
            dataUnits = params[2];
            daysCount = params[3];

            log.v("Data passed to internal variables successfully!");

            fetchWeatherJsonData();

            log.v("Start parsing weather data...");
            parseForecastJsonStr();

            return null;
        }

        private String forecastJsonStr;

        public void fetchWeatherJsonData() {

            HttpURLConnection urlConnection = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                String weatherFetchingUrl = buildWeatherFetchingUrl();

                URL url = new URL(weatherFetchingUrl);

                log.v("URL: \"" + weatherFetchingUrl + "\"");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                log.v("Connection opened..");

                //read url stream into String
                forecastJsonStr = getForecastJsonStringFromUrlConnection(urlConnection);

            } catch (IOException e) {
                log.e("Error :" + e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            this.forecastJsonStr = forecastJsonStr;
        }

        private String getForecastJsonStringFromUrlConnection
                (HttpURLConnection urlConnection) {

            StringBuilder jsonStrBuffer = new StringBuilder();
            BufferedReader buffReader = null;

            try {
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) return null;

                buffReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = buffReader.readLine()) != null) {
                    jsonStrBuffer.append(line).append("\n");
                }
            } catch (IOException e) {
                log.e(e.getMessage());

                //try closing non-null buffReader
                if (buffReader != null)
                    try {
                        buffReader.close();
                    } catch (IOException e1) {
                        log.e("Error closing buffered reader: " + e1.getMessage());
                    }
            }

            return jsonStrBuffer.toString();
        }

        private String buildWeatherFetchingUrl() {
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("http");
            uriBuilder.authority("api.openweathermap.org");

            uriBuilder
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("forecast")
                    .appendPath("daily");
            uriBuilder
                    .appendQueryParameter("q", cityName)
                    .appendQueryParameter("mode", dataMode)
                    .appendQueryParameter("units", dataUnits)
                    .appendQueryParameter("cnt", daysCount)
                    .appendQueryParameter("appid", getString(R.string.owm_appid));

            return uriBuilder.build().toString();
        }

        private void parseForecastJsonStr() {
            try {
                JSONObject mainJson = new JSONObject(forecastJsonStr);

                parseCityAndCountryName(mainJson.getJSONObject("city"));

                parseWeatherData(mainJson.getJSONArray("list"));
            } catch (JSONException e) {
                log.e("Couldn't parse forecastJsonStr or/and display its content: " + e.getMessage());
            }

        }

        private String activityTitle;

        private void parseCityAndCountryName(JSONObject cityJsonObj) {
            try {
                String cityName = cityJsonObj.getString("name");
                String countryName = cityJsonObj.getString("country");

                String activityTitle = cityName + ", " + countryName;
                log.i("City name title: " + activityTitle);

                this.activityTitle = activityTitle;
            } catch (JSONException e) {
                log.e("Couldn't parse city/country name(s): " + e.getMessage());
            }
        }

        private ArrayList<String> forecastAListStr;

        private void parseWeatherData(JSONArray weatherDataJsonArray) {

            ArrayList<String> forecastAListStr = new ArrayList<>();
            try {
                for (int i = 0; i < weatherDataJsonArray.length(); i++) {

                    Date weatherDate = getWeatherDayDate(weatherDataJsonArray.getJSONObject(i));

                    JSONObject dayTempJsonObj = weatherDataJsonArray.getJSONObject(i).getJSONObject("temp");
                    Long maxTemp = getDayMaxTemp(dayTempJsonObj);
                    Long minTemp = getDayMinTemp(dayTempJsonObj);
                    Long dayTemp = getDayTemp(dayTempJsonObj);

                    DateFormat dateFormat = new SimpleDateFormat("EEEE d/M");
                    String dayForecastRow = (new StringBuilder())
                            .append(dateFormat.format(weatherDate))
                            .append(" - ")
                            .append("Day: ")
                            .append(dayTemp)
                            .append(", ")
                            .append("Max: ")
                            .append(maxTemp)
                            .append(", ")
                            .append("Min: ")
                            .append(minTemp).toString();

                    log.i("Finished parsing day weather: " + dayForecastRow);

                    forecastAListStr.add(dayForecastRow);

                }

            } catch (JSONException e) {
                log.e("Couldn't parse weather data: " + e.getMessage());
            }

            this.forecastAListStr = forecastAListStr;
        }

        private Date getWeatherDayDate(JSONObject dayWeatherObj) {
            Date weatherDate = null;
            try {

                weatherDate = new Date(dayWeatherObj.getLong("dt") * 1000);

            } catch (JSONException e) {
                log.e("Couldn't parse weather date: " + e.getMessage());
            }

            return weatherDate;
        }

        private Long getDayMaxTemp(JSONObject dayTempJsonObj) {
            Long dayMaxTemp = null;
            try {
                dayMaxTemp = dayTempJsonObj.getLong("max");
            } catch (JSONException e) {
                log.e("Couldn't parse maximum temp of the day: " + e.getMessage());
            }

            return dayMaxTemp;
        }

        private Long getDayMinTemp(JSONObject dayTempJsonObj) {
            Long dayMinTemp = null;
            try {
                dayMinTemp = dayTempJsonObj.getLong("min");
            } catch (JSONException e) {
                log.e("Couldn't parse minimum temp of the day: " + e.getMessage());
            }

            return dayMinTemp;
        }

        private Long getDayTemp(JSONObject dayTempJsonObj) {
            Long dayTemp = null;
            try {
                dayTemp = dayTempJsonObj.getLong("day");
            } catch (JSONException e) {
                log.e("Couldn't parse mid-day temp of the day: " + e.getMessage());
            }

            return dayTemp;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (forecastJsonStr.equals("") || (forecastJsonStr == null))
                return;
            log.v("Start populating weather data to UI...");

            setActivityTitleToCityName();

            populateWeatherArrayToUI();
        }

        private void populateWeatherArrayToUI() {
            forecastArrayAdapter.clear();
            for (String dayWeather : forecastAListStr) {
                forecastArrayAdapter.add(dayWeather);
            }
        }

        private void setActivityTitleToCityName() {
            try {
                getActivity().setTitle(activityTitle);
            } catch (Exception e) {
                log.e("Couldn't change activity title to city name: " + e.getMessage());
            }
        }
    }

}
