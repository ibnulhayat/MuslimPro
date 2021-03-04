package com.muslimguide.androidapp.models;

public class DuaItem {

    String duaId;
    String duaName;
    String duaImage;

    public DuaItem(String duaId, String duaName, String duaImage) {
        this.duaId = duaId;
        this.duaName = duaName;
        this.duaImage = duaImage;
    }

    public String getDuaId() {
        return duaId;
    }

    public void setDuaId(String duaId) {
        this.duaId = duaId;
    }

    public String getDuaName() {
        return duaName;
    }

    public void setDuaName(String duaName) {
        this.duaName = duaName;
    }

    public String getDuaImage() {
        return duaImage;
    }

    public void setDuaImage(String duaImage) {
        this.duaImage = duaImage;
    }
}
