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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        // Set the adapter on the {@link ListView}
        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(adapter);

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

    }

    private void updateUi(Earthquake earthquake) {
        // Update the UI with the earthquake data we got from the API
    }

    private class GetDataAsync extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Create URL object
            URL url = urls[0];
            // Perform HTTP request to the URL and receive a JSON response back
            // Extract relevant fields from the JSON response and create an {@link Event} object
            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Update the UI
        }

        private URL createUrl(String stringUrl) {
            // Convert the string we start with into a URL
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.v(LOG_TAG, "Error creating the URL");
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            // Setup the jsonResponse, urlConnection, inputStream
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            // Check if URL exists before anything else
            if (url == null) {
                return jsonResponse;
            }
            // Connection code
            // Set up the request object
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000); // milliseconds
            urlConnection.setConnectTimeout(15000); // milliseconds
            try {
                // And attempt the connection
                urlConnection.connect();
                // If successful, get the input stream
                switch (urlConnection.getResponseCode()) {
                    case 200:
                        Log.v(LOG_TAG, "200 response from server");
                        // get the input stream
                        inputStream = urlConnection.getInputStream();
                        // convert the stream of bytes to a String
                        jsonResponse = readFromStream(inputStream);
                        break;
                    case 404:
                        Log.v(LOG_TAG, "404 response from server");
                        break;
                    default:
                        jsonResponse = "";
                        break;
                }
            } catch (IOException) {
                Log.v(LOG_TAG, "Failure to establish the connection");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            // Use a string builder to gradually convert the JSON response to a String
            StringBuilder jsonResponse = new StringBuilder();
            if (inputStream != null) {
                // InputStreamReader converts the bytes read by the InputStream into char
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                // BufferedReader adds buffering capabilities, speeding up the process
                BufferedReader reader = new BufferedReader(inputStreamReader);
                // readLine() returns everything until the line break
                // if there is nothing it returns null
                String line = reader.readLine();
                while (line != null) {
                    // gradually build the string with the json response
                    jsonResponse.append(line);
                    line = reader.readLine();
                }
            }
            return jsonResponse.toString();
        }

        private Earthquake extractEartquakeFromJson(String jsonResponse) {
            // Parse the JSON response and create the list of earthquakes
        }

    }

}
