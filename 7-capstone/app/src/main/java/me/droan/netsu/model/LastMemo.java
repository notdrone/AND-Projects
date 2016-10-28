package me.droan.netsu.model;

/**
 * Created by Drone on 22/09/16.
 */

public class LastMemo {
    private String text;
    private String timeAt;


    public LastMemo() {
    }

    public LastMemo(String text, String timeAt) {
        this.text = text;
        this.timeAt = timeAt;
    }

    public String getText() {
        return text;
    }

    public String getTimeAt() {
        return timeAt;
    }
}
