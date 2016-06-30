package com.example.kedee.mistu.profile;


public class HelperDetailListItem {
    private int helperId;
    private String name;
    private String branchStream;

    public HelperDetailListItem(int helperId, String branchStream, String name) {
        this.helperId = helperId;
        this.branchStream = branchStream;
        this.name = name;
    }

    public String getBranchStream() {
        return branchStream;
    }

    public void setBranchStream(String branchStream) {
        this.branchStream = branchStream;
    }

    public int getHelperId() {
        return helperId;
    }

    public void setHelperId(int helperId) {
        this.helperId = helperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
