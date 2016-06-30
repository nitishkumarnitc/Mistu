package com.example.kedee.mistu.profile;


public class ProfileListItem {
    private int helpId,userId;

    private String title;
    private String description;
    private String category;
    private String tag1,tag2,tag3;

    private String fname,lname;
    private String sex;
    private String branch,stream;

    public ProfileListItem(String branch, String category, String description, String fname,
                           int helpId, String lname, String stream, String sex, String tag1,
                           String tag2, String tag3, String title, int userId) {
        this.branch = branch;
        this.category = category;
        this.description = description;
        this.fname = fname;
        this.helpId = helpId;
        this.lname = lname;
        this.stream = stream;
        this.sex = sex;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.title = title;
        this.userId = userId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getHelpId() {
        return helpId;
    }

    public void setHelpId(int helpId) {
        this.helpId = helpId;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
