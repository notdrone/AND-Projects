package me.droan.netsu.model;

/**
 * Created by Drone on 15/09/16.
 */

public class User {
    private String displayName;
    private String email;
    private String uid;
    private String photoUrl;

    public User() {
    }

    public User(String uid, String displayName, String email, String photoUrl) {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }

}
