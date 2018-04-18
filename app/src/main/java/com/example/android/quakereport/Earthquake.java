package com.example.android.quakereport;

/**
 * mMag corresponds to the magnitude of the earthquake
 * mCity is the closest city to the earthquake
 * mDate is the date of occurrence
 * mUrl is the url used to get the data
 * */
public class Earthquake {
    private double mMag;
    private String mCity;
    private long mDate;
    private String mUrl;

    public Earthquake(double mag, String city, long date, String url) {
        mMag = mag;
        mCity = city;
        mDate = date;
        mUrl = url;
    }

    public long getmDate() {
        return mDate;
    }

    public String getmCity() {
        return mCity;
    }

    public double getmMag() {
        return mMag;
    }

    public String getmUrl() { return mUrl; }
}
