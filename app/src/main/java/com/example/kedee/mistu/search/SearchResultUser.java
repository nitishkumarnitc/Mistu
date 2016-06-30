package com.example.kedee.mistu.search;


public class SearchResultUser {
    private String name;
    private String branchStream;
    private boolean hasUserConfirmed;
    private int userId;

    public SearchResultUser(String branchStream, Boolean hasUserConfirmed, String name,int userId) {
        this.branchStream = branchStream;
        this.hasUserConfirmed = hasUserConfirmed;
        this.name = name;
        this.userId=userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBranchStream() {
        return branchStream;
    }

    public void setBranchStream(String branchStream) {
        this.branchStream = branchStream;
    }

    public Boolean getHasUserConfirmed() {
        return hasUserConfirmed;
    }

    public void setHasUserConfirmed(Boolean hasUserConfirmed) {
        this.hasUserConfirmed = hasUserConfirmed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
