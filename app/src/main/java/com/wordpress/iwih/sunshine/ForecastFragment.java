package com.wordpress.iwih.sunshine;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
                R.id.forecast_text_view);

        ListView forecastListView = (ListView) inflate.findViewById(R.id.forecast_list_view);
        forecastListView.setAdapter(forecastArrayAdapter);
        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callDetailActivity((TextView) view);
            }
        });

        return inflate;

    }

    private void callDetailActivity(TextView view) {
        Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
                .putExtra(Intent.EXTRA_TEXT, view.getText());

        log.i("Starting DetailActivity...");
        startActivity(detailActivityIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchWeatherData();
    }

    public void fetchWeatherData() {
        if (!NetworkUtilities.isNetworkAvailable(getActivity(), true))
            return;

        log.v("Calling FetchWeatherAsync...");
        String ironDefaultValue = getString(R.string.pref_location_default);
        String location = SettingsActivity.getStringPreferences(
                getActivity(),
                getString(R.string.pref_location_key),
                ironDefaultValue);
        if (location.equals(""))
            location = ironDefaultValue;
        String units = SettingsActivity.getStringPreferences(
                getActivity(),
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_default));
        new FetchWeatherAsync(getActivity()).execute(location, "json", units, "7");
    }


}
