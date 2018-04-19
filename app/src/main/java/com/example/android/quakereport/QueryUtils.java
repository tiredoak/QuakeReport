package com.example.android.quakereport;

import android.util.Log;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Earthquake> fetchEarthquakeData(String stringUrl) throws IOException {

        URL url = createUrl(stringUrl);
        String jsonResponse = makeHttpRequest(url);
        return extractEarthquakes(jsonResponse);
    }


    // Helper method to convert a string into a URL
    private static URL createUrl(String stringUrl) {
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

    // Returns the JSON response as a String
    private static String makeHttpRequest(URL url) throws IOException {
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
        } catch (IOException e) {
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

    // Helper method to convert response from bytes to string
    private static String readFromStream(InputStream inputStream) throws IOException {
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


    private static List<Earthquake> extractEarthquakes(String jsonResponse) {

        Log.v("extractEarthquakes", jsonResponse);
        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject raw = new JSONObject(jsonResponse);
            JSONArray features = raw.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject singleEarthquake = (JSONObject) features.get(i);
                JSONObject singleEarthquakeProperties = singleEarthquake.getJSONObject("properties");

                double mag = singleEarthquakeProperties.getDouble("mag");
                String city = singleEarthquakeProperties.getString("place");
                long date = singleEarthquakeProperties.getLong("time");
                String url = singleEarthquakeProperties.getString("url");

                Earthquake earthquake = new Earthquake(mag, city, date, url);
                earthquakes.add(earthquake);
            }

            Log.v("Parser", String.valueOf(earthquakes));
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}