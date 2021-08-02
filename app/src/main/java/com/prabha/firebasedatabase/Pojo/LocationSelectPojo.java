package com.prabha.firebasedatabase.Pojo;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class LocationSelectPojo {
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String location;
    public boolean selected = false;
}
