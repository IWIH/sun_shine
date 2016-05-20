package com.wordpress.iwih.sunshine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_main extends Fragment {


    public fragment_main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_main, container, false);

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
                        R.layout.fragment_list_item_forecast,
                        R.id.forecast_text_view,
                        forecastList);

        ((ListView) getActivity().findViewById(R.id.forecast_list_view)).setAdapter(forecastAdapter);

        // Inflate the layout for this fragment
        return inflate;

    }

}
