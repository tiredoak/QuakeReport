package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
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

        Earthquake currentEarthquake = (Earthquake) getItem(position);

        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag_text_view);
        magTextView.setText(String.valueOf(currentEarthquake.getmMag()));

        TextView cityTextView = (TextView) listItemView.findViewById(R.id.city_text_view);
        cityTextView.setText(currentEarthquake.getmCity());

        Date dateObject = new Date(currentEarthquake.getmDate());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        String date = formatDate(dateObject);
        dateTextView.setText(date);

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        String time = formatTime(dateObject);
        timeTextView.setText(time);

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
}
