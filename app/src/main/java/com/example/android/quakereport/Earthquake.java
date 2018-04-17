package com.example.android.quakereport;

public class Earthquake {
    private double mMag;
    private String mCity;
    private long mDate;

    public Earthquake(double mag, String city, long date) {
        mMag = mag;
        mCity = city;
        mDate = date;
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
}
