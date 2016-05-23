package com.wordpress.iwih.sunshine;


import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    final Logger log = new Logger("ForecastFragment");

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.forecast_fragment, container, false);
        Log.v("forecast_fragment", "Fragment Inflated!");
        String[] weekForecastArray = {
                "Saturday",
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday"};

        List<String> forecastList =
                new ArrayList<>(Arrays.asList(weekForecastArray));

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.forecast_list_item_fragment,
                        R.id.forecast_text_view,
                        forecastList);

        ListView forecastListView = (ListView) inflate.findViewById(R.id.forecast_list_view);
        forecastListView.setAdapter(forecastAdapter);

        // Inflate the layout for this fragment
        return inflate;

    }

    public void fetchWeatherData() {
        log.v("Calling FetchWeatherAsync...");
        new FetchWeatherAsync().execute("As Samawah", "json", "metric", "7");
    }

    private class FetchWeatherAsync extends AsyncTask<String, Void, Void> {

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
            return null;
        }

        public String fetchWeatherJsonData() {

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

            return forecastJsonStr;
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
    }

}
