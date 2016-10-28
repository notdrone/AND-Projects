package me.droan.netsu.model;

/**
 * Created by Drone on 20/09/16.
 */

public class About {
    private String name;
    private String trackerId;
    private String photoUrl;
    private String childId;

    public About() {
    }

    public About(Child child, String childId) {
        this.name = child.getName();
        this.trackerId = child.getTrackerId();
        this.photoUrl = child.getPhotoUrl();
        this.childId = childId;
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

    public String getChildId() {
        return childId;
    }
}
