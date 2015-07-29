package com.yahoo.shopping.instagramviewer;

import java.util.Date;
import java.util.List;

/**
 * Created by jamesyan on 7/28/15.
 */
public class InstagramModel {
    private String link;
    private String imageUrl;
    private Date createTIme;
    private String title;
    private String profilePicUrl;
    private String userName;
    private int likeCounts;
    private List<Comment> comments;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreateTIme() {
        return createTIme;
    }

    public void setCreateTIme(Date createTIme) {
        this.createTIme = createTIme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(int likeCounts) {
        this.likeCounts = likeCounts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
