package edu.gvsu.cis.hw_10;

import org.parceler.Parcel;

@Parcel
public class LocationLookup {
    double origLat;
    double origLng;
    double endLat;
    double endLng;

    public double getOrigLat() {
        return origLat;
    }

    public double getOrigLng() {
        return origLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public double getEndLng() {
        return endLng;
    }
}
