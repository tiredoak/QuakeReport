package com.example.android.quakereport;

/**
 * mMag corresponds to the magnitude of the earthquake
 * mCity is the closest city to the earthquake
 * mDate is the date of occurrence
 * mUrl is the url used to get the data
 * */
public class Earthquake {
    private double mMag;
    private String mPlace;
    private long mDate;
    private String mUrl;

    public Earthquake(double mag, String place, long date, String url) {
        mMag = mag;
        mPlace = place;
        mDate = date;
        mUrl = url;
    }

    public long getmDate() {
        return mDate;
    }

    public String getmPlace() {
        return mPlace;
    }

    public double getmMag() {
        return mMag;
    }

    public String getmUrl() { return mUrl; }

    @Override
    public String toString() {
        return "mMag = " + mMag + "\n" +
                "mPlace = " + mPlace + "\n" +
                "mDate = " + mDate + "\n" +
                "mUrl = " + mUrl + "\n";
    }
}
