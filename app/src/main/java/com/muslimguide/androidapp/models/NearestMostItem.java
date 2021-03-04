package com.muslimguide.androidapp.models;

public class NearestMostItem {
    String placeName;
    String placeDistance;
    String image_link;

    public NearestMostItem(String placeName, String placeDistance, String image_link) {
        this.placeName = placeName;
        this.placeDistance = placeDistance;
        this.image_link = image_link;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceDistance() {
        return placeDistance;
    }

    public void setPlaceDistance(String placeDistance) {
        this.placeDistance = placeDistance;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
