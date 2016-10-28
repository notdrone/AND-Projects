package me.droan.netsu.model;


import android.net.Uri;

public class Child {
    private String name;
    private String trackerId;
    private LastMedicine lastMedicine;
    private String photoUrl;

    private LastTemperature lastTemperature;

    public Child() {
    }

    public Child(String name, String trackerId, Uri photoUri, LastTemperature lastTemperature, LastMedicine lastMedicine) {
        this.name = name;
        this.trackerId = trackerId;
        if (photoUri != null) {
            this.photoUrl = photoUri.toString();
        }
        this.lastTemperature = lastTemperature;
        this.lastMedicine = lastMedicine;
    }

    public String getName() {
        return name;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public LastTemperature getLastTemperature() {
        return lastTemperature;
    }
}
