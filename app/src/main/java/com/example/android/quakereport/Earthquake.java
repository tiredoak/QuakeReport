package com.example.android.quakereport;

public class Earthquake {
    private double mMag;
    private String mCity;
    private String mDate;

    public Earthquake(double mag, String city, String date) {
        mMag = mag;
        mCity = city;
        mDate = date;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmCity() {
        return mCity;
    }

    public double getmMag() {
        return mMag;
    }
}
