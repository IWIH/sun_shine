package com.wordpress.iwih.sunshine;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    final private Logger log = new Logger(this.getClass().getSimpleName());
    private TextView forecastDetailTextView;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatorView = inflater.inflate(R.layout.fragment_detail, container, false);
        log.v("DetailFragment inflated successfully..");

        forecastDetailTextView = (TextView) inflatorView.findViewById(R.id.detail_activity_forecast_textview);

        populateForecastToUI(getForecastFromIntent());
        return inflatorView;
    }

    private void populateForecastToUI(String forecastToPopulate) {

        if (forecastDetailTextView == null) {
            log.v("Couldn't get and cast detail_activity_forecast_textview.");
            return;
        }

        forecastDetailTextView.setText(forecastToPopulate);
        log.i("detail_activity_forecast_textview got the forecastStr.");
    }

    private String getForecastFromIntent() {
        Bundle extrasBundle = getActivity().getIntent().getExtras();

        if (extrasBundle == null) {
            log.v("Couldn't get and cast .getIntent.getExtras() into bundle for extras package!");
            return null;
        }
        String forecastStr;
        forecastStr = extrasBundle.getString(Intent.EXTRA_TEXT);
        log.i("Forecast string had been passed successfully to DetailActivity.");
        return forecastStr;
    }

}
