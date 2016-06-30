package com.example.kedee.mistu.profile;


public class ShowInterestsListItem {
    private String name;
    private boolean isCheckBoxClicked;

    public ShowInterestsListItem(boolean isCheckBoxClicked, String name) {
        this.isCheckBoxClicked = isCheckBoxClicked;
        this.name = name;
    }

    public boolean isCheckBoxClicked() {
        return isCheckBoxClicked;
    }

    public void setCheckBoxClicked(boolean checkBoxClicked) {
        isCheckBoxClicked = checkBoxClicked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
