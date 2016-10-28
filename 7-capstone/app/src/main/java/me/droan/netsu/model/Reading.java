package me.droan.netsu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by Drone on 16/09/16.
 */

public class Reading {
    /**
     * type 0= temperature
     * type 1= memo
     * type 2= medicine
     */


    private int type;
    private double temperature;
    private String memo;
    private String medicine;
    private long reverseTimeStamp;

    public Reading() {
    }

    public Reading(double temperature, long time) {
        this.type = 0;
        this.reverseTimeStamp = convertReverseTimeStamp(time);
        this.temperature = temperature;
    }

    public Reading(int type, String str, long time) {
        if (type == 1) {
            this.memo = str;
        } else {
            this.medicine = str;
        }
        this.type = type;
        this.reverseTimeStamp = convertReverseTimeStamp(time);
    }

    public long getReverseTimeStamp() {
        return reverseTimeStamp;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getMemo() {
        return memo;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getType() {
        return type;
    }

    @JsonIgnore
    private long convertReverseTimeStamp(long time) {
        return -1 * new Date(time).getTime();
    }

}
