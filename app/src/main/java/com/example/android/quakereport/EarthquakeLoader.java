package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl == null || mUrl == "") {
            return null;
        }

        List<Earthquake> earthquakes = null;
        try {
            earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return earthquakes;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
