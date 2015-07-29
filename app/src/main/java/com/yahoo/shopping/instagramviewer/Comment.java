package com.yahoo.shopping.instagramviewer;

/**
 * Created by jamesyan on 7/29/15.
 */
public class Comment {
    private String userName;
    private String profilePicUrl;
    private String comment;

    public Comment(String userName, String profilePicUrl, String comment) {
        this.userName = userName;
        this.profilePicUrl = profilePicUrl;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
