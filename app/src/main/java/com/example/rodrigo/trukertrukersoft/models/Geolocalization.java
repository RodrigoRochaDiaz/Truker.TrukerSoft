package com.example.rodrigo.trukertrukersoft.models;

/**
 * Created by Rodrigo on 01/04/2017.
 */

public class Geolocalization {
    private double latitude;
    private double longitude;

    public Geolocalization() {
    }

    public Geolocalization(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
