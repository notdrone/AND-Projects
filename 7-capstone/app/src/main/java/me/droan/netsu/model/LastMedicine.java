package me.droan.netsu.model;

/**
 * Created by Drone on 22/09/16.
 */

public class LastMedicine {
    private String name;
    private long timeAt;
    private long timeNext;

    public LastMedicine() {
    }

    public LastMedicine(String name, long timeAt, long timeNext) {
        this.name = name;
        this.timeAt = timeAt;
        this.timeNext = timeNext;
    }

    public String getName() {
        return name;
    }

    public long getTimeAt() {
        return timeAt;
    }

    public long getTimeNext() {
        return timeNext;
    }

}
