package com.example.kedee.mistu.notification;

public class NotiACCListItem {
    private String name;
    private String branchStream;
    private float rating;

    private String category;
    private String title;
    private String description;
    private String tag1,tag2,tag3;

    private int helpId,helperId,currentUserId;
    private int hasContacted,hasSkipped;
    private int notiId;



    public NotiACCListItem(String branchStream, String category, int currentUserId,
                           String description, int hasContacted, int hasSkipped, int helperId,
                           int helpId, String name, int notiId, float rating, String tag1, String tag2,
                           String tag3, String title) {

        this.branchStream = branchStream;
        this.category = category;
        this.currentUserId = currentUserId;
        this.description = description;
        this.hasContacted = hasContacted;
        this.hasSkipped = hasSkipped;
        this.helperId = helperId;
        this.helpId = helpId;
        this.name = name;
        this.notiId=notiId;

        this.rating = rating;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.title = title;
    }



    public int getNotiId() {
        return notiId;
    }

    public void setNotiId(int notiId) {
        this.notiId = notiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchStream() {
        return branchStream;
    }

    public void setBranchStream(String branchStream) {
        this.branchStream = branchStream;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public int getHelpId() {
        return helpId;
    }

    public void setHelpId(int helpId) {
        this.helpId = helpId;
    }

    public int getHelperId() {
        return helperId;
    }

    public void setHelperId(int helperId) {
        this.helperId = helperId;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public int getHasContacted() {
        return hasContacted;
    }

    public void setHasContacted(int hasContacted) {
        this.hasContacted = hasContacted;
    }

    public int getHasSkipped() {
        return hasSkipped;
    }

    public void setHasSkipped(int hasSkipped) {
        this.hasSkipped = hasSkipped;
    }
}
