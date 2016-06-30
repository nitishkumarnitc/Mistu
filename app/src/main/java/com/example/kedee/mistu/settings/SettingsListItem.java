package com.example.kedee.mistu.settings;


public class SettingsListItem {
    private String itemName;
    private int picId;

    public SettingsListItem(String itemName, int picId) {
        this.itemName = itemName;
        this.picId = picId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }
}
