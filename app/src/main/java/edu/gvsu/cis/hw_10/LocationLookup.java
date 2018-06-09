package edu.gvsu.cis.hw_10;

import org.joda.time.DateTime;
import org.parceler.Parcel;

@Parcel
public class LocationLookup {
    String _key;
    double origLat;
    double origLng;
    double endLat;
    double endLng;
    DateTime _timeStamp;

    public DateTime get_timeStamp() {
        return _timeStamp;
    }

    public String getKey() {
        return _key;
    }

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

    public void set_timeStamp(DateTime _timeStamp) {
        this._timeStamp = _timeStamp;
    }

    public void setKey(String _key) {
        this._key = _key;
    }

    public void setOrigLat(double origLat) {
        this.origLat = origLat;
    }

    public void setOrigLng(double origLng) {
        this.origLng = origLng;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }
}
