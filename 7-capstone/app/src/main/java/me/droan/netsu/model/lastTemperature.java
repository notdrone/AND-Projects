package me.droan.netsu.model;

/**
 * Created by Drone on 20/09/16.
 */

public class LastTemperature {

    private double temperature;
    private long timeAt;
    private long timeNext;

    public LastTemperature() {
    }

    public LastTemperature(double temperature, long timeAt, long timeNext) {
        this.temperature = temperature;
        this.timeAt = timeAt;
        this.timeNext = timeNext;
    }

    public double getTemperature() {
        return temperature;
    }

    public long getTimeAt() {
        return timeAt;
    }

    public long getTimeNext() {
        return timeNext;
    }
}
