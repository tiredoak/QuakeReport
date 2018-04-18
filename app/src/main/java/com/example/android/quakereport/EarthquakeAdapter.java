package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter {
    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if convertView already exists (was recycled) or if it's null
        // If so, we must inflate a new view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Grab the current earthquake being used
        Earthquake currentEarthquake = (Earthquake) getItem(position);

        // Set the magnitude in the UI, after formatting it correctly
        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag_text_view);
        String mag = formatMagnitude(currentEarthquake.getmMag());
        magTextView.setText(mag);

        // Split location String into two separate strings, one for location offset and one
        // for the primary location
        String rawString = currentEarthquake.getmCity();
        String locationOffset = getLocationOffset(rawString);
        String primaryLocation = getPrimaryLocation(rawString);

        // Set location strings in the UI
        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.location_offset_text_view);
        locationOffsetTextView.setText(locationOffset);

        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location_text_view);
        primaryLocationTextView.setText(primaryLocation);

        // Set the date in the UI, after formatting it correctly
        // Date is split in date and time for the UI
        Date dateObject = new Date(currentEarthquake.getmDate());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        String date = formatDate(dateObject);
        dateTextView.setText(date);

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        String time = formatTime(dateObject);
        timeTextView.setText(time);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        return dateFormatter.format(date);
    }

    private String formatTime(Date date) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        return timeFormatter.format(date);
    }

    private String getLocationOffset(String raw) {
        // Check string format type.
        // If no km from a specific location is provided, return "near the"
        if (!raw.contains("km")) {
            return "Near the";
        } else {
            // The string has more detailed information
            return raw.substring(0, raw.indexOf("of") + 2);
        }
    }

    private String getPrimaryLocation(String raw) {
        if (!raw.contains("km")) {
            return raw;
        } else {
            return raw.substring(raw.indexOf("of") + 3, raw.length());
        }
    }

    private String formatMagnitude(double mag) {
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        return decimalFormatter.format(mag);
    }

    private int getMagnitudeColor(double mag) {
        // Round the magnitude value to determine color
        int roundMagnitude = (int) Math.round(mag);
        int color;
        switch (roundMagnitude) {
            case 1:
                color = ContextCompat.getColor(getContext(), R.color.magnitude1);
                return color;
            case 2:
                color = ContextCompat.getColor(getContext(), R.color.magnitude2);
                return color;
            case 3:
                color = ContextCompat.getColor(getContext(), R.color.magnitude3);
                return color;
            case 4:
                color = ContextCompat.getColor(getContext(), R.color.magnitude4);
                return color;
            case 5:
                color = ContextCompat.getColor(getContext(), R.color.magnitude5);
                return color;
            case 6:
                color = ContextCompat.getColor(getContext(), R.color.magnitude6);
                return color;
            case 7:
                color = ContextCompat.getColor(getContext(), R.color.magnitude7);
                return color;
            case 8:
                color = ContextCompat.getColor(getContext(), R.color.magnitude8);
                return color;
            case 9:
                color = ContextCompat.getColor(getContext(), R.color.magnitude9);
                return color;
            default:
                color = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                return color;
        }
    }
}
