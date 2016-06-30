package com.example.kedee.mistu.notification;


public class NotiREQListItem {
    private int notiId;
    private String name;
    private String branchStream;
    private String category;
    private String title;
    private String description;
    private String tag1,tag2,tag3;
    private int helpId,helpieId,currentUserId;
    private int isAccepted;
    //not required now
    private int secondsPassed; // seconds passed since request generated
    private int picId;


    public NotiREQListItem(int notiId,String name, String branchStream, String category, String title,
                           String description, String tag1, String tag2, String tag3, int helpId,
                           int helpieId, int currentUserId, int isAccepted) {
        this.notiId=notiId;
        this.name = name;
        this.branchStream = branchStream;
        this.category = category;
        this.title = title;
        this.description = description;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.helpId = helpId;
        this.helpieId = helpieId;
        this.currentUserId = currentUserId;
        this.isAccepted = isAccepted;
    }

    public NotiREQListItem(String name, String branchStream, String category, String title) {
        this.name = name;
        this.branchStream = branchStream;
        this.category = category;
        this.title = title;
    }

    public int getNotiId() {
        return notiId;
    }

    public void setNotiId(int notiId) {
        this.notiId = notiId;
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

    public int getHelpieId() {
        return helpieId;
    }

    public void setHelpieId(int helpieId) {
        this.helpieId = helpieId;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }


    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
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

    public int getSecondsPassed() {
        return secondsPassed;
    }

    public void setSecondsPassed(int secondsPassed) {
        this.secondsPassed = secondsPassed;
    }

}

