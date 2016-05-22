package com.wordpress.iwih.sunshine;


import android.app.Fragment;
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

    final String LOG_TAG = "ForecastFragment";
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

    public void fetchWeatherData() throws IOException {
        new FetchWeatherAsync().execute();
    }

    private class FetchWeatherAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getWeatherData();
            return null;
        }

        public String getWeatherData() {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                //http://api.openweathermap.org/data/2.5/forecast/daily?q=as samawah&mode=json&units=metric&cnt=7&appid=c298ffc0ae3c0785df75268904871c9b
                URL url = new URL("http://www.google.com");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                log.v("URL: \"" + urlConnection.getURL().toString() + "\"");
                urlConnection.connect();
                log.v("Connection opened..");

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder jsonStrBuffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    jsonStrBuffer.append(line).append("\n");
                }

                if (jsonStrBuffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = jsonStrBuffer.toString();
            } catch (IOException e) {
                Log.e("forecast_fragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return forecastJsonStr;
        }
    }

}
