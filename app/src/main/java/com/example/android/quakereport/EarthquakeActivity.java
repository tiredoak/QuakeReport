/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        // Get a list of earthquake locations.
        List<Earthquake> earthquakes = new ArrayList<>();

        // Set the adapter on the {@link ListView}
        mAdapter = new EarthquakeAdapter(this, earthquakes);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(mAdapter);

        // Click listener to launch the browser to the URL showing more
        // information regarding the earthquake
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake = (Earthquake) parent.getItemAtPosition(position);
                Uri webPage = Uri.parse(earthquake.getmUrl());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, webPage);
                if (launchBrowser.resolveActivity(getPackageManager()) != null) {
                    startActivity(launchBrowser);
                }
            }
        });

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Earthquake> earthquakes = null;
            try {
                earthquakes = QueryUtils.fetchEarthquakeData(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            super.onPostExecute(earthquakes);
            // Update the UI
            mAdapter.clear();
            if (earthquakes != null && !earthquakes.isEmpty()) {
                mAdapter.addAll(earthquakes);
            }
        }
    }
}
